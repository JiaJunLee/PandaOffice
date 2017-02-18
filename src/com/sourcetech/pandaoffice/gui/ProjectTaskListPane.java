package com.sourcetech.pandaoffice.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sourcetech.pandaoffice.dao.Project;
import com.sourcetech.pandaoffice.dao.RSAInfo;
import com.sourcetech.pandaoffice.dao.Task;
import com.sourcetech.pandaoffice.dao.TaskList;
import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STGridPane;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.util.FontUtil;
import com.sourcetech.pandaoffice.util.HTTPUtil;
import com.sourcetech.pandaoffice.util.ImageUtil;
import com.sourcetech.pandaoffice.util.UIFactory.DefaultTextField;
import com.sourcetech.pandaoffice.util.safe.Panda_RSA_Algorithm;
import com.sourcetech.pandaoffice.widget.STImageButton;
import com.sourcetech.pandaoffice.widget.STUserIcon;
import com.sourcetech.pandaoffice.widget.ScrollTest.MyScrollBar;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class ProjectTaskListPane extends STPanel{
	
	private static final int LIST_WIDTH = 310;
	private static final int LIST_HEIGHT = 490;
	
	private static final Color nameColor = STColor.decode("#464545");
	private static final Font nameFont = FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 16.5f);
	private static final Font nameTaskFont = FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 15f);
	
	private ArrayList<TaskList> taskLists;
	
	public ProjectTaskListPane(ArrayList<TaskList> taskLists) {
		super(true);
		setLayout(null);
		this.taskLists = taskLists;
		
		init();
	}
	
	private void init(){
		STGridPane gridPane = new STGridPane(taskLists.size(),20,20,true);
		
		JScrollPane scrollPane = new JScrollPane(gridPane);
		scrollPane.setBorder(null);
		scrollPane.setBackground(null);
		scrollPane.setBounds(0, 0, 920, 515);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(50);
		scrollPane.getHorizontalScrollBar().setBackground(STColor.ALPHA);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		for(int i=0;i<taskLists.size();i++){
			ListPane listPane = new ListPane(taskLists.get(i),i+1);
			gridPane.add(listPane);
		}
		
		gridPane.autoSize();
		gridPane.setBounds(0, 0, gridPane.getPreferredSize().width, LIST_HEIGHT);
		add(scrollPane);
	}
	
	class TaskPane extends STPanel{
		
		private Task task;
		private BufferedImage image;
		
		public TaskPane(final Task task) {
			super(new File(Res.Mipmap(Res.TextView_Default_Background)));
			setPreferredSize(new Dimension(260, 70));
			setLayout(null);
			this.task = task;
			
			try {
				image = ImageIO.read(new File(Res.Mipmap(Res.User_Icon)));
				image = ImageUtil.getRoundedImage(image, 36, 36, 2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			this.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					StringBuilder builder = new StringBuilder();
					builder.append("任务名称:  " + task.getName());
					builder.append("\n");
					builder.append("任务创建时间:  " + task.getCreateDateTime());
					builder.append("\n");
					builder.append("任务截止时间:  " + task.getOverdueDateTime());
					builder.append("\n");
					builder.append("创建者ID:  " + task.getCreateUserId());
					builder.append("\n");
					builder.append("执行者ID:  " + task.getExecuteUserId());
					builder.append("\n");
					JOptionPane.showMessageDialog(null, builder.toString());
				}
			});
			
		}
		
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			graphics2d.setColor(STColor.GRAY_STANDARD);
			graphics2d.setFont(nameTaskFont);
			graphics2d.drawString(task.getName(), 25, 39);
			
			graphics2d.drawImage(image, null, 204, 17);
		}
		
	}
	
	class ListPane extends STPanel{
		
		private TaskList taskList;
		private STImageButton createButton;
		private int index;
		
		private void init(){
			DefaultTextField listName = new DefaultTextField(taskList.getName() + "·" + index + " >", nameFont, nameColor, false);
			listName.autoSize();
			listName.setBounds(20, 15, listName.getPreferredSize().width, listName.getPreferredSize().height);
			add(listName);
				
			STGridPane gridPane = new STGridPane(1, 10, 10, true);
			ArrayList<Task> tasks = getTasks(taskList);
			
			JScrollPane scrollPane = new JScrollPane(gridPane);
			scrollPane.setBorder(null);
			scrollPane.setBackground(null);
			scrollPane.setBounds(20, 50, 280, 430);
			scrollPane.getVerticalScrollBar().setUnitIncrement(50);
			scrollPane.getVerticalScrollBar().setBackground(STColor.ALPHA);
			scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 430));
			scrollPane.getVerticalScrollBar().setUI(new MyScrollBar());
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
			
			
			for(Task t : tasks){
				gridPane.add(new TaskPane(t));
			}
			gridPane.autoSize();
			add(scrollPane);
			
			createButton = STImageButton
					.newBuilder()
					.setIcon(Res.Mipmap(Res.Create_Task_btn))
					.setShowShadow(true)
					.setButtonPadding(2)
					.autoSize(270, 15)
					.build();
			add(createButton);
			
			createButton.addButtonListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					String datas[] = CreateTaskDialog.showCreateProjectDialog();
					Task task = new Task();
					task.setName(datas[0]);
					task.setMarks(datas[1]);
					task.setListId(taskList.getId());
					SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					try {
						task.setCreateDateTime(new Timestamp(formater.parse(datas[2]).getTime()));
						task.setOverdueDateTime(new Timestamp(formater.parse(datas[3]).getTime()));
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					task.setExecuteUserId(4);
					task.setRank(0);
					task.setType(0);
					task.setCreateUserId(4);
					createTask(task);
					removeAll();
					init();
				}
			});
		}
		
		public ListPane(final TaskList taskList,int index) {
			super(new File(Res.Mipmap(Res.Task_List_Background)));
			setPreferredSize(new Dimension(LIST_WIDTH, LIST_HEIGHT));
			setLayout(null);
			this.taskList = taskList;
			this.index = index;
			init();
		}
		
		private int createTask(Task task){
			JsonObject requestInfo = new JsonObject();
			requestInfo.addProperty("action", 1);
			JsonParser jsonParser = new JsonParser();
			Gson gson = new Gson();
			JsonObject obj = (JsonObject) jsonParser.parse(gson.toJson(task));
			requestInfo.add("task", obj);
			RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
			String str = (String)HTTPUtil.request(Res.Url(Res.Task_Servlet), rsaInfo);
			JsonObject ro = (JsonObject) jsonParser.parse(str);
			return ro.get("id").getAsInt();
		}
		
		public ArrayList<Task> getTasks(TaskList list){
			JsonObject requestInfo = new JsonObject();
			requestInfo.addProperty("action", 0);
			requestInfo.addProperty("list_id", list.getId());
			RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
			JsonParser jsonParser = new JsonParser();
			String str = (String) HTTPUtil.request(Res.Url(Res.Task_Servlet), rsaInfo);
			JsonObject obj = (JsonObject) jsonParser.parse(str);
			JsonArray array = obj.get("tasks").getAsJsonArray();
			ArrayList<Task> tasks = new ArrayList<Task>();
			Gson gson = new Gson();
			for (JsonElement e : array) {
				Task task = gson.fromJson(e, Task.class);
				tasks.add(task);
			}
			return tasks;
		}
		
	}

}
