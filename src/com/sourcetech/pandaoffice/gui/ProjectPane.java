package com.sourcetech.pandaoffice.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JScrollPane;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sourcetech.pandaoffice.dao.Member;
import com.sourcetech.pandaoffice.dao.Project;
import com.sourcetech.pandaoffice.dao.RSAInfo;
import com.sourcetech.pandaoffice.dao.TaskGroup;
import com.sourcetech.pandaoffice.dao.TransferFile;
import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STGridPane;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.util.FileUtil;
import com.sourcetech.pandaoffice.util.HTTPUtil;
import com.sourcetech.pandaoffice.util.ImageUtil;
import com.sourcetech.pandaoffice.util.safe.Panda_RSA_Algorithm;
import com.sourcetech.pandaoffice.view.Tween;
import com.sourcetech.pandaoffice.view.ViewPager;
import com.sourcetech.pandaoffice.widget.STTimeView;
import com.sourcetech.pandaoffice.widget.STWorkTitle;
import com.sourcetech.pandaoffice.widget.STImageButton;
import com.sourcetech.pandaoffice.widget.STLoadDialog;
import com.sourcetech.pandaoffice.widget.STSwitchButton.TestFrame;
import com.sourcetech.pandaoffice.widget.ScrollTest.MyScrollBar;

public class ProjectPane extends STPanel {

	private Member member;
	private ViewPager viewPager;
	private ArrayList<STPanel> panes;

	public ProjectPane(Member member) {
		super(new File(Res.Mipmap(Res.Main_Frame_Work_Pane_Background)));
		setLayout(null);
		this.member = member;

		initViewPager();
		initProjects();

		viewPager.setSelectedPage(0);
	}

	private void initViewPager() {
		viewPager = ViewPager.newBuilder()
				.setBounds(new Rectangle(15,15,MainFrame.Work_Pane_Width-30,MainFrame.Work_Pane_Height-30))
				.setPageCount(2).setSpeed(30).setTween(Tween.Function_Cubic, Tween.Type_Easy_In).build();
		panes = viewPager.getPanes();
		for(STPanel p :panes){
			p.setLayout(null);
		}
		add(viewPager);
	}

	private void initProjects() {

		STLoadDialog loadDialog = new STLoadDialog("获取项目", null);
		loadDialog.showDialog();

		STPanel projectPane = new STPanel(true);
		projectPane.setBounds(0, 0, MainFrame.Work_Pane_Width, MainFrame.Work_Pane_Height);
		projectPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane(projectPane);
		scrollPane.setBorder(null);
		scrollPane.setBackground(null);
		scrollPane.setBounds(92, 18, 840, 510);
		scrollPane.getVerticalScrollBar().setUnitIncrement(50);
		scrollPane.getVerticalScrollBar().setBackground(STColor.ALPHA);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, MainFrame.Work_Pane_Height));
		scrollPane.getVerticalScrollBar().setUI(new MyScrollBar());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		STWorkTitle personTitle = new STWorkTitle("个人项目");
		personTitle.setBounds(0, 0);
		projectPane.add(personTitle);

		final ArrayList<Project> personProjects = getPersonProjects();
		
		STImageButton[] items = new STImageButton[personProjects.size()];
		STGridPane gridPane = new STGridPane(3, 34, 24, true);

		for (int i = 0; i < personProjects.size(); i++) {
			BufferedImage image;
			try {
				image = ImageIO.read(getFile(personProjects.get(i).getIcon()));
				image = ImageUtil.getProjectImage(image, personProjects.get(i).getName());
				items[i] = STImageButton.newBuilder().setIcon(image).setShowShadow(true).setButtonPadding(0)
						.autoSize(0, 0).build();
				final int index = i;
				items[i].addButtonListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						ProjectDetailPane projectDetailPane = new ProjectDetailPane(personProjects.get(index),ProjectPane.this);
						projectDetailPane.setBounds(0, 0, 1000, 700);
						panes.get(1).removeAll();
						panes.get(1).add(projectDetailPane);
						viewPager.setSelectedPage(1);
					}
				});
				gridPane.add(items[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			BufferedImage image = ImageIO.read(new File(Res.Mipmap(Res.Create_Project_Img)));
			image = ImageUtil.getProjectImage(image, "");
			STImageButton createProjectButton = STImageButton.newBuilder().setIcon(image).setShowShadow(true).setButtonPadding(0)
					.autoSize(0, 0).build();
			gridPane.add(createProjectButton);
			createProjectButton.addButtonListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 创建项目
					String[] projectInfo = CreateProjectDialog.showCreateProjectDialog();
					if(projectInfo!=null){
						Project project = new Project();
						project.setType(0);
						project.setName(projectInfo[0]);
						project.setUserId(4);
						project.setIcon("801a7d71-6cf3-4be4-a5fa-a37370891d7e");
						int projectId = createProject(project);
						TaskGroup taskGroup = new TaskGroup();
						taskGroup.setProjectId(projectId);
						taskGroup.setName("默认分组");
						taskGroup.setIntroduce("默认分组");
						createTaskGroup(taskGroup);
						panes.get(0).removeAll();
						initProjects();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		gridPane.autoSize();
		Dimension dimension = gridPane.getPreferredSize();
		gridPane.setBounds(0, 45, dimension.width, dimension.height);
		projectPane.setPreferredSize(
				new Dimension(dimension.width, dimension.height + personTitle.getPreferredSize().height + 80));
		projectPane.add(gridPane);
		panes.get(0).add(scrollPane);

		loadDialog.setVisible(false);
	}
	
	public void back(){
		viewPager.setSelectedPage(0);
	}
	
	private int createProject(Project project){
		JsonObject requestInfo = new JsonObject();
		requestInfo.addProperty("action", 1);
		JsonParser jsonParser = new JsonParser();
		Gson gson = new Gson();
		JsonObject obj = (JsonObject) jsonParser.parse(gson.toJson(project));
		requestInfo.add("project", obj);
		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
		String str = (String)HTTPUtil.request(Res.Url(Res.Project_Servlet), rsaInfo);
		JsonObject ro = (JsonObject) jsonParser.parse(str);
		return ro.get("id").getAsInt();
	}
	
	private void createTaskGroup(TaskGroup taskGroup){
		JsonObject requestInfo = new JsonObject();
		requestInfo.addProperty("action", 1);
		JsonParser jsonParser = new JsonParser();
		Gson gson = new Gson();
		JsonObject obj = (JsonObject) jsonParser.parse(gson.toJson(taskGroup));
		requestInfo.add("group", obj);
		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
		HTTPUtil.request(Res.Url(Res.TaskGroup_Servlet), rsaInfo);
	}

	private ArrayList<Project> getPersonProjects() {
		JsonObject requestInfo = new JsonObject();
		requestInfo.addProperty("user_id", member.getUserId());
		requestInfo.addProperty("action", 0);
		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
		JsonParser jsonParser = new JsonParser();
		String str = (String) HTTPUtil.request(Res.Url(Res.Project_Servlet), rsaInfo);
		JsonObject obj = (JsonObject) jsonParser.parse(str);
		JsonArray array = obj.get("projects").getAsJsonArray();
		ArrayList<Project> projects = new ArrayList<Project>();
		Gson gson = new Gson();
		for (JsonElement e : array) {
			Project project = gson.fromJson(e, Project.class);
			projects.add(project);
		}
		return projects;
	}

	private File getFile(String fileId) {
		JsonObject requestInfo = new JsonObject();
		requestInfo.addProperty("file_id", fileId);
		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
		TransferFile transferFile = (TransferFile) HTTPUtil.request(Res.Url(Res.Download_Servlet), rsaInfo);
		return FileUtil.byte2File(transferFile.getFile(), transferFile.getInfo(), Res.Thumb_Project_Image_Dic);
	}

}
