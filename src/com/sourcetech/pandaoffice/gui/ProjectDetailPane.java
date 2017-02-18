package com.sourcetech.pandaoffice.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileSystemView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sourcetech.pandaoffice.dao.FileRelate;
import com.sourcetech.pandaoffice.dao.FileShare;
import com.sourcetech.pandaoffice.dao.Project;
import com.sourcetech.pandaoffice.dao.RSAInfo;
import com.sourcetech.pandaoffice.dao.Task;
import com.sourcetech.pandaoffice.dao.TaskGroup;
import com.sourcetech.pandaoffice.dao.TaskList;
import com.sourcetech.pandaoffice.dao.TransferFile;
import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STGridPane;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.util.FileUtil;
import com.sourcetech.pandaoffice.util.FontUtil;
import com.sourcetech.pandaoffice.util.HTTPUtil;
import com.sourcetech.pandaoffice.util.UIFactory.DefaultTextField;
import com.sourcetech.pandaoffice.util.safe.EncryptFactory;
import com.sourcetech.pandaoffice.util.safe.Panda_RSA_Algorithm;
import com.sourcetech.pandaoffice.view.ImageButtonGroup.ButtonGroupListener;
import com.sourcetech.pandaoffice.view.TabView;
import com.sourcetech.pandaoffice.view.Tween;
import com.sourcetech.pandaoffice.view.ViewPager;
import com.sourcetech.pandaoffice.view.TabView.TabViewListener;
import com.sourcetech.pandaoffice.widget.STCutline;
import com.sourcetech.pandaoffice.widget.STImageButton;
import com.sourcetech.pandaoffice.widget.STImageView;
import com.sourcetech.pandaoffice.widget.STLoadDialog;
import com.sourcetech.pandaoffice.widget.STTaskGroupList;
import com.sourcetech.pandaoffice.widget.STTextButton;
import com.sourcetech.pandaoffice.widget.ScrollTest.MyScrollBar;

public class ProjectDetailPane extends STPanel{
	
	private Project project;
	private ArrayList<TaskGroup> groups;
	private DefaultTextField groupName;
	private ViewPager viewPager;
	private ArrayList<STPanel> panes;
	
	private static final Color nameColor = STColor.decode("#464545");
	private static final Font font = FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 18);
	private static final Font nameFont = FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 16);
	private static final Font groupFont = FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 17);
	
	public ProjectDetailPane(Project project, final ProjectPane projectPane) {
		super(true);
		setLayout(null);
		this.project = project;
		
		initTaskGroups();
		initViewPager();
		
		STImageButton backButton = STImageButton.newBuilder().setIcon(Res.Mipmap(Res.Project_Detail_Back_btn)).autoSize(45, 18).build();
		add(backButton);
		
		STImageButton taskButton = STImageButton.newBuilder().setIcon(Res.Mipmap(Res.TaskGroup_Detail_Back_btn)).autoSize(265, 18).build();
		add(taskButton);
		
		TabView tabView = TabView.newBuilder().setButtonTexts(new String[]{"概 览","统 计","分 享","文 件","日 程","群 聊"})
				 .setButtonPadding(new Insets(5, 22, 5, 22))
				 .setLineHeight(3)
				 .setForegroundColor(STColor.GRAY_DEEP)
				 .setFocusColor(STColor.DEFAULT_BLUE)
				 .setFont(font)
				 .build();
		tabView.autoSize(380, 0);
		add(tabView);
		
		DefaultTextField projectName = new DefaultTextField(project.getName(), nameFont, nameColor, false);
		projectName.autoSize();
		projectName.setBounds(82, 11, projectName.getPreferredSize().width, projectName.getPreferredSize().height);
		add(projectName);
		
		groupName = new DefaultTextField(groups.get(0).getName(), groupFont, STColor.GRAY_STANDARD, false);
		groupName.autoSize();
		groupName.setBounds(82, 32, groupName.getPreferredSize().width, groupName.getPreferredSize().height);
		add(groupName);
		
		tabView.addTabViewListener(new TabViewListener() {
			public void ButtonSelect(int select, MouseEvent e) {
				viewPager.setSelectedPage(select);
				panes.get(select).removeAll();
				switch(select){
				case 0:
					initList();
					break;
				case 1:
					initStatistics();
					break;
				case 2:
					initSharePage();
					break;
				case 3:
					initFileSharePage();
				}
			}
		});
		
		backButton.addButtonListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				projectPane.back();
			}
		});
		
		taskButton.addButtonListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				STTaskGroupList groupList = new STTaskGroupList(ProjectDetailPane.this, groups);
				groupList.addSelectListener(new ButtonGroupListener() {
					public void OnSelected(int index) {
						ArrayList<TaskList> lists = getTaskList(groups.get(index));
						ProjectTaskListPane listPane = new ProjectTaskListPane(lists);
						listPane.setBounds(0, 0, 920, 510);
						panes.get(0).removeAll();
						panes.get(0).add(listPane);
					}
					public void OnMetaDown(int index) {
						
					}
				});
				groupList.setLocation(543, 330);
				groupList.setVisible(true);
			}
		});
		
		initList();
		
		initStatistics();
		
		initSharePage();
		
		initFileSharePage();
	}
	
	private void initFileSharePage(){
		JLayeredPane[] tp = new JLayeredPane[2];
		for (int i = tp.length - 1; i >= 0; i--) {
			tp[i] = new JLayeredPane();
			tp[i].setBackground(STColor.ALPHA);
			tp[i].setBounds(0, 0, 260, 520);
			panes.get(3).add(tp[i]);
		}
	
		STImageView bg = new STImageView(Res.File_Bg_Background);
		bg.setBounds(0, 0, 248, 512);
		tp[0].add(bg);
		
		STImageButton uploadButton = STImageButton
				.newBuilder()
				.setIcon(Res.Mipmap(Res.Upload_Btn))
				.setShowShadow(true)
				.setButtonPadding(2)
				.setText("   上传文件")
				.setTextColor(STColor.WHITE)
				.setTextFont(nameFont)
				.setLayout(STImageButton.HORIZONAL)
				.autoSize(18, 265)
				.build();
		tp[1].add(uploadButton);
		
		ArrayList<FileShare> fileShares = getFileShares();
		STGridPane gridPane = new STGridPane(4, 10, 10, true);
		for(final FileShare fileShare:fileShares){
			STImageButton button = STImageButton
					.newBuilder()
					.setIcon(getTypeIcon("??").getAbsolutePath())
					.setText(fileShare.getName())
					.setShowShadow(true)
					.setButtonPadding(10)
					.autoSize(0, 0)
					.build();
			button.addButtonListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					int n = JOptionPane.showConfirmDialog(null, "是否下载?", "询问",JOptionPane.YES_NO_OPTION);
					if(n==JOptionPane.YES_OPTION){
						File dir = showDirChooserDialog();
						FileRelate fileRelate = getFileRelate(fileShare);
						getFile(fileRelate.getFileId(), dir.getAbsolutePath());
						JOptionPane.showMessageDialog(null, "下载完成");
					}
				};
			});
			gridPane.add(button);
		}
		gridPane.autoSize();
		gridPane.setBounds(265, 5, gridPane.getPreferredSize().width, gridPane.getPreferredSize().height);
		panes.get(3).add(gridPane);
		uploadButton.addButtonListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				File file = showFileChooserDialog();
				try {
					updateFile(file);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "上传失败");
					e1.printStackTrace();
				}
			}
		});
	}
	
	private FileRelate getFileRelate(FileShare fileShare){
		JsonObject requestInfo = new JsonObject();
		requestInfo.addProperty("share_id", fileShare.getId());
		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
		JsonParser jsonParser = new JsonParser();
		String str = (String) HTTPUtil.request(Res.Url(Res.FileRelate_Servlet), rsaInfo);
		JsonObject obj = (JsonObject) jsonParser.parse(str);
		Gson gson = new Gson();
		return gson.fromJson(obj.get("relate").getAsJsonObject(), FileRelate.class);
	}
	
	private File getFile(String fileId, String path) {
		JsonObject requestInfo = new JsonObject();
		requestInfo.addProperty("file_id", fileId);
		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
		TransferFile transferFile = (TransferFile) HTTPUtil.request(Res.Url(Res.Download_Servlet), rsaInfo);
		return FileUtil.byte2File(transferFile.getFile(), transferFile.getInfo(), path);
	}
	
	private File showDirChooserDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}
	
	private File showFileChooserDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}
	
	public void updateFile(File file) throws IOException{
		
		byte[] fileByte = FileUtil.getContent(file);
		
		com.sourcetech.pandaoffice.dao.File fileInfo = new com.sourcetech.pandaoffice.dao.File();
		
		
		fileInfo.setId(UUID.randomUUID().toString());
		fileInfo.setMd5(EncryptFactory.toHexString(EncryptFactory.getInstance().toHashBytes(fileByte, EncryptFactory.MD5_ALGORITHM)));
		fileInfo.setName(FileUtil.getName(file));
		fileInfo.setSize((double)fileByte.length/1024/1024);
		fileInfo.setType(FileUtil.getType(file));
		
		final TransferFile transferFile = new TransferFile();
		transferFile.setFile(fileByte);
		transferFile.setInfo(fileInfo);
		
		JsonObject object = new JsonObject();
		object.addProperty("user_id", 4);
		object.addProperty("project_id", project.getId());
		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(object);
		transferFile.setRsaInfo(rsaInfo);
		new Thread(){public void run() {
			STLoadDialog loadDialog = new STLoadDialog("上传中", null);
			loadDialog.showDialog();
			HTTPUtil.request(Res.Url(Res.Upload_Servlet), transferFile);
			loadDialog.setVisible(false);
			panes.get(3).removeAll();
			initFileSharePage();
		};}.start();
		
	}
	
	public ArrayList<FileShare> getFileShares(){
		ArrayList<FileShare> fileShares = new ArrayList<FileShare>();
		JsonObject requestInfo = new JsonObject();
		requestInfo.addProperty("project_id", project.getId());
		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
		JsonParser jsonParser = new JsonParser();
		String str = (String) HTTPUtil.request(Res.Url(Res.File_Servlet), rsaInfo);
		JsonObject obj = (JsonObject) jsonParser.parse(str);
		JsonArray array = obj.get("shares").getAsJsonArray();
		Gson gson = new Gson();
		for (JsonElement e : array) {
			FileShare fileShare = gson.fromJson(e, FileShare.class);
			fileShares.add(fileShare);
		}
		return fileShares;
	}
	
	private File getTypeIcon(String type){
		String path = Res.Dic(Res.File_Detail_Dic);
		if(!type.equals("doc") && !type.equals("docx") && !type.equals("xls") && !type.equals("xlsx") && !type.equals("ppt") && !type.equals("pptx") && !type.equals("pdf"))
			path += "\\other.png";
		else{
			path += "\\";
			path += type;
			path += ".png";
		}
		return new File(path);
	}
	
	private void initSharePage(){
		DefaultTextField defaultTextField = new DefaultTextField("暂不可用", font, STColor.GRAY_STANDARD, false);
		defaultTextField.autoSize();
		defaultTextField.setBounds(150, 150, defaultTextField.getPreferredSize().width, defaultTextField.getPreferredSize().height);;
		panes.get(2).add(defaultTextField);
	}
	
	private void initStatistics() {
		DefaultTextField[] textFields = {
				new DefaultTextField("任务分布图例", groupFont, STColor.DEFAULT_BLUE, false),
				new DefaultTextField("今天的任务", groupFont, STColor.DEFAULT_BLUE, false),
				new DefaultTextField("未完成的任务", groupFont, STColor.DEFAULT_BLUE, false)
		};
		for(DefaultTextField dtf : textFields)
			dtf.autoSize();
		textFields[0].setBounds(0, 15, textFields[0].getPreferredSize().width, textFields[0].getPreferredSize().height);
		textFields[1].setBounds(0, 90, textFields[1].getPreferredSize().width, textFields[1].getPreferredSize().height);
		textFields[2].setBounds(0, 255, textFields[2].getPreferredSize().width, textFields[2].getPreferredSize().height);
		for(DefaultTextField dtf : textFields)
			panes.get(1).add(dtf);
		
		ArrayList<Task> allTask = getAllTasks(lists);
		long WEEK = 1000 * 60 * 60 * 24 * 7;
		int finish = 0;
		int recent = 0;
		int willExpire = 0;
		int overdue = 0;
		
		ArrayList<Task> notFinishTasks = new ArrayList<Task>();
		
		for(Task task : allTask){
			switch(task.getType()){
			case 0:
				notFinishTasks.add(task);
				break;
			case 2:
				finish ++;
				break;
				default:
					if(task.getOverdueDateTime().before(new Timestamp(System.currentTimeMillis())))
						overdue ++;
					if(task.getOverdueDateTime().before(new Timestamp(System.currentTimeMillis() + WEEK)))
						willExpire ++;
					break;
			}
		}
		recent = allTask.size() - finish - willExpire - overdue;
		STCutline cutline = new STCutline(finish, recent, willExpire, overdue, new Rectangle(0, 45, 900, 26));
		panes.get(1).add(cutline);
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formater2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Timestamp todayEnd = null;
		try {
			todayEnd = new Timestamp(formater2.parse(formater.format(new Date())+ " 23:59:59").getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ArrayList<Task> todayTasks = new ArrayList<Task>();
		for(Task task : allTask){
			if(task.getOverdueDateTime().before(todayEnd) && task.getType()!=2)
				todayTasks.add(task);
		}
		
		STGridPane todayList = new STGridPane(1, 10, 10, true);
		
		JScrollPane todayListScrollPane = new JScrollPane(todayList);
		todayListScrollPane.setBorder(null);
		todayListScrollPane.setBackground(STColor.WHITE);
		todayListScrollPane.setBounds(0, 115, 900, 140);
		todayListScrollPane.getVerticalScrollBar().setUnitIncrement(50);
		todayListScrollPane.getVerticalScrollBar().setBackground(STColor.ALPHA);
		todayListScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 125));
		todayListScrollPane.getVerticalScrollBar().setUI(new MyScrollBar());
		todayListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		for(Task task:todayTasks){
			todayList.add(new TaskListItem(task));
		}
		todayList.autoSize();
		todayList.setBounds(0, 110, todayList.getPreferredSize().width, todayList.getPreferredSize().height);
		panes.get(1).add(todayListScrollPane);
		
		STGridPane notFinishList = new STGridPane(1, 10, 10, true);
		
		JScrollPane notFinishListScrollPane = new JScrollPane(notFinishList);
		notFinishListScrollPane.setBorder(null);
		notFinishListScrollPane.setBackground(STColor.WHITE);
		notFinishListScrollPane.setBounds(0, 280, 900, 150);
		notFinishListScrollPane.getVerticalScrollBar().setUnitIncrement(50);
		notFinishListScrollPane.getVerticalScrollBar().setBackground(STColor.ALPHA);
		notFinishListScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 130));
		notFinishListScrollPane.getVerticalScrollBar().setUI(new MyScrollBar());
		notFinishListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		for(Task task:notFinishTasks){
			notFinishList.add(new TaskListItem(task));
		}
		notFinishList.autoSize();
		notFinishList.setBounds(0, 110, notFinishList.getPreferredSize().width, notFinishList.getPreferredSize().height);
		panes.get(1).add(notFinishListScrollPane);
	}
	
	class TaskListItem extends STPanel{
		public TaskListItem(Task task) {
			super(true);
			setPreferredSize(new Dimension(870, 35));
			setLayout(null);
			SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			STTextButton button = STTextButton.newBuilder().setText(task.getName() + "  截至日期: " + formater.format(task.getOverdueDateTime().getTime()))
					.setFont(FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 14))
					.setBackgroundColor(STColor.ALPHA)
					.setForegroundColor(STColor.GRAY_STANDARD)
					.setShowShadow(true)
					.setRadius(10f)
					.autoSize(20, 0, new Insets(5, 10, 5, 10))
					.build();
			add(button);
		}
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.setColor(STColor.GRAY_STANDARD);
			graphics2d.drawLine(0, 34, 870, 34);
		}
	}
	
	public ArrayList<Task> getAllTasks(ArrayList<TaskList> lists){
		ArrayList<Task> tasks = new ArrayList<Task>();
		for(TaskList list : lists){
			JsonObject requestInfo = new JsonObject();
			requestInfo.addProperty("action", 0);
			requestInfo.addProperty("list_id", list.getId());
			RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
			JsonParser jsonParser = new JsonParser();
			String str = (String) HTTPUtil.request(Res.Url(Res.Task_Servlet), rsaInfo);
			JsonObject obj = (JsonObject) jsonParser.parse(str);
			JsonArray array = obj.get("tasks").getAsJsonArray();
			Gson gson = new Gson();
			for (JsonElement e : array) {
				Task task = gson.fromJson(e, Task.class);
				tasks.add(task);
			}
		}
		return tasks;
	}
	
	private ArrayList<TaskList> lists;

	private void initList(){
		lists = getTaskList(groups.get(0));
		ProjectTaskListPane listPane = new ProjectTaskListPane(lists);
		listPane.setBounds(0, 0, 920, 510);
		panes.get(0).removeAll();
		panes.get(0).add(listPane);
	}
	
	private void initViewPager() {
		viewPager = ViewPager.newBuilder()
				.setBounds(new Rectangle(45,63,920,600))
				.setPageCount(6).setSpeed(30).setTween(Tween.Function_Cubic, Tween.Type_Easy_In).build();
		panes = viewPager.getPanes();
		for(STPanel p :panes){
			p.setLayout(null);
		}
		add(viewPager);
		
	}
	
	private ArrayList<TaskList> getTaskList(TaskGroup taskGroup){
		JsonObject requestInfo = new JsonObject();
		requestInfo.addProperty("group_id", taskGroup.getId());
		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
		JsonParser jsonParser = new JsonParser();
		String str = (String) HTTPUtil.request(Res.Url(Res.TaskList_Servlet), rsaInfo);
		JsonObject obj = (JsonObject) jsonParser.parse(str);
		JsonArray array = obj.get("task_lists").getAsJsonArray();
		ArrayList<TaskList> lists = new ArrayList<TaskList>();
		Gson gson = new Gson();
		for (JsonElement e : array) {
			TaskList list = gson.fromJson(e, TaskList.class);
			lists.add(list);
		}
		return lists;
	}

	private void initTaskGroups() {
		JsonObject requestInfo = new JsonObject();
		requestInfo.addProperty("action", 0);
		requestInfo.addProperty("project_id", project.getId());
		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
		JsonParser jsonParser = new JsonParser();
		String str = (String) HTTPUtil.request(Res.Url(Res.TaskGroup_Servlet), rsaInfo);
		JsonObject obj = (JsonObject) jsonParser.parse(str);
		JsonArray array = obj.get("task_groups").getAsJsonArray();
		groups = new ArrayList<TaskGroup>();
		Gson gson = new Gson();
		for (JsonElement e : array) {
			TaskGroup group = gson.fromJson(e, TaskGroup.class);
			groups.add(group);
		}
	}
	
	public void setSelect(int index){
		groupName.setText(groups.get(index).getName());
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D graphics2d = (Graphics2D) g;
		g.setColor(STColor.GRAY_DEEP);
		g.drawLine(310, 35, 960, 35);
	}

}
