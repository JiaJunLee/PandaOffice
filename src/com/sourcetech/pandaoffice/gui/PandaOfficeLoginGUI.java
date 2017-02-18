package com.sourcetech.pandaoffice.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sourcetech.pandaoffice.dao.Captche;
import com.sourcetech.pandaoffice.dao.Member;
import com.sourcetech.pandaoffice.dao.MemberInfo;
import com.sourcetech.pandaoffice.dao.RSAInfo;
import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STFrame;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.util.FontUtil;
import com.sourcetech.pandaoffice.util.HTTPUtil;
import com.sourcetech.pandaoffice.util.NetworkInfoUtil;
import com.sourcetech.pandaoffice.util.safe.Base64;
import com.sourcetech.pandaoffice.util.safe.Panda_RSA_Algorithm;
import com.sourcetech.pandaoffice.view.TabView;
import com.sourcetech.pandaoffice.view.Tween;
import com.sourcetech.pandaoffice.view.ViewPager;
import com.sourcetech.pandaoffice.view.TabView.TabViewListener;
import com.sourcetech.pandaoffice.widget.STImageView;
import com.sourcetech.pandaoffice.widget.STLoadDialog;
import com.sourcetech.pandaoffice.widget.STTextButton;
import com.sourcetech.pandaoffice.widget.STTextField;
import com.sourcetech.pandaoffice.widget.ZHCanvasEffects;

import jdk.nashorn.internal.scripts.JO;

public class PandaOfficeLoginGUI extends STFrame {

	private static final long serialVersionUID = 7735403353401725160L;

	private static final Font DEFAULT_TEXT_FONT = FontUtil.getFont(
			Res.Font(Res.FZ_LTH), 0, 16);
	private static final Font SMALL_TEXT_FONT = FontUtil.getFont(
			Res.Font(Res.FZ_LTH), 0, 13);

	/**
	 * Pane初始化数量
	 */
	public static final int Pane_Count = 3;

	/**
	 * Frame尺寸
	 */
	public static final int Frame_Width = 530;
	public static final int Frame_Height = 650;

	/**
	 * 内边距
	 */
	public static final int offset_X = 15;
	public static final int offset_Y = 15;

	public static final int Animation_Update_Time = 28;

	private JLayeredPane[] panes = null;
	private ViewPager viewPager = null;

	public PandaOfficeLoginGUI() {
		super(Pane_Count, Frame_Width, Frame_Height);
		setDefaultCloseOperation(STFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// 获取画布
		panes = getPanes();

		// 初始化背景
		STPanel panel = new STPanel(new File(
				Res.Mipmap(Res.STPanel_Default_Background)));
		panel.setBounds(0, 0, Frame_Width, Frame_Height);

		panes[0].add(panel);

		// 初始化动画控件
		final ZHCanvasEffects canvasEffects = new ZHCanvasEffects(
				new Rectangle(offset_X, offset_Y, Frame_Width - 2 * offset_X,
						Frame_Height - 2 * offset_Y));
		canvasEffects.setBackground(Color.WHITE);
		new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(Animation_Update_Time);
						canvasEffects.update();
						repaint(); // 防止顶层pane不显示
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();

		// 加入背景动画
		panes[1].add(canvasEffects);

		// 加入LOGO
		STImageView logoImage = new STImageView(Res.Panda_Office_Logo);
		logoImage.setBounds((Frame_Width - 264) / 2, 60, 264, 69);
		panes[2].add(logoImage);

		// 初始化TabView
		TabView tabView = TabView
				.newBuilder()
				.setButtonTexts(
						new String[] { Res.Text(Res.Login_Tab_View_login_text),
								Res.Text(Res.Login_Tab_View_Register_text) })
				.setForegroundColor(STColor.GRAY)
				.setSelectColor(STColor.DEFAULT_BLUE)
				.setButtonPadding(new Insets(0, 26, 8, 26)).setLineHeight(3)
				.build();
		tabView.autoSize((Frame_Width - 188) / 2, 172);
		tabView.addTabViewListener(new TabViewListener() {
			public void ButtonSelect(int select, MouseEvent e) {
				System.out.println(select);
				viewPager.setSelectedPage(select);
			}
		});
		panes[2].add(tabView);

		// 初始化ViewPager
		initViewPager();

		// 源创TIP
		STTextButton company = STTextButton.newBuilder().onlyText()
				.setText("@2016 源创").setShowShadow(false)
				.setForegroundColor(STColor.GRAY_LIGHT)
				.setFocusColor(STColor.GRAY_STANDARD)
				.setFont(DEFAULT_TEXT_FONT)
				.autoSize((Frame_Width - 94) / 2, 577, new Insets(0, 0, 0, 0))
				.build();
		panes[2].add(company);

	}
	
	private JTextComponent[] loginTexts;
	private JTextComponent[] regTexts;

	private void initViewPager() {
		// 初始化
		viewPager = ViewPager
				.newBuilder()
				.setBounds(
						new Rectangle((Frame_Width - 360) / 2, 245, 360, 300))
				.setPageCount(2).setSpeed(30)
				.setTween(Tween.Function_Cubic, Tween.Type_Easy_In).build();

		// 获取ViewPage的页面
		ArrayList<STPanel> viewPanels = viewPager.getPanes();

		/* 登录页初始化 */

		// 创建输入框
		STTextField loginTextField = STTextField
				.newBuilder()
				.setTexts(new String[] { "", "" })
				.setHints(new String[] { "手机号或邮箱", "密码" })
				.setEnables(new boolean[] { true, true })
				.setTypes(
						new int[] { STTextField.DEFAULT, STTextField.PASSWORD })
				.autoSize(0, 12, new Dimension(360, 61))
				.setPadding(new Insets(5, 30, 5, 30)).build();
		loginTexts = loginTextField.getTextFields();
		viewPanels.get(0).add(loginTextField);

		// 创建登录按钮
		STTextButton loginButton = STTextButton.newBuilder()
				.setText(Res.Text(Res.Login_Button_Login_text)).setRadius(10)
				.autoSize(0, 158, new Insets(12, 156, 12, 156)).build();
		viewPanels.get(0).add(loginButton);

		// 忘记密码
		STTextButton forgetPassword = STTextButton.newBuilder().onlyText()
				.setText("忘记密码?").setShowShadow(false)
				.setForegroundColor(STColor.GRAY_STANDARD)
				.setFont(DEFAULT_TEXT_FONT)
				.autoSize(286, 234, new Insets(0, 0, 0, 0)).build();
		viewPanels.get(0).add(forgetPassword);

		/* 注册页初始化 */
		STTextField regTextField = STTextField
				.newBuilder()
				.setTexts(new String[] { "", "", "" })
				.setHints(new String[] { "姓名", "手机号", "密码(不小于 6 位)" })
				.setEnables(new boolean[] { true, true, true })
				.setTypes(
						new int[] { STTextField.DEFAULT, STTextField.DEFAULT,
								STTextField.PASSWORD })
				.autoSize(0, 0, new Dimension(360, 50))
				.setPadding(new Insets(5, 30, 5, 30)).build();
		regTexts = regTextField.getTextFields();
		viewPanels.get(1).add(regTextField);

		// 协议提醒
		STTextButton protocolText = STTextButton.newBuilder().onlyText()
				.setText("点击「注册」按钮，即代表你同意《熊猫办公协议》").setShowShadow(false)
				.setForegroundColor(STColor.GRAY_STANDARD)
				.setFont(SMALL_TEXT_FONT)
				.autoSize(30, 252, new Insets(0, 0, 0, 0)).build();
		viewPanels.get(1).add(protocolText);

		// 创建注册按钮
		STTextButton regButton = STTextButton.newBuilder()
				.setText(Res.Text(Res.Login_Button_Register_text))
				.setRadius(10).autoSize(0, 174, new Insets(12, 156, 12, 156))
				.build();
		viewPanels.get(1).add(regButton);
		
		// 添加登陆按钮监听器
		loginButton.addButtonListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				toLogin();
			}
		});
		
		// 添加注册按钮监听器
		regButton.addButtonListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// 检查用户输入的注册信息
				boolean isRightful = validateRegTexts();
				if(isRightful){
					// 请求验证码
					boolean captcheRequest = requestCaptche();
					if(captcheRequest){
						String captche = JOptionPane.showInputDialog("输入验证码:");
						boolean registerRequest = registerMember(captche);
						if(registerRequest){
							JOptionPane.showMessageDialog(null, "注册成功!");
							// 未完成
							// 加入自动登录功能
						}
					}
				}
			}
		});

		// 加入ViewPager
		panes[2].add(viewPager);
	}
	
	public void toLogin(){
		for(JTextComponent component:loginTexts){
			if(component.getText().isEmpty()){
				JOptionPane.showMessageDialog(null, "请完整填写所有的表单项目！", "警告", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		String userPE = loginTexts[0].getText();
		String password = new String(((JPasswordField)loginTexts[1]).getPassword());
		loginMember(userPE, password);
	}

	public void loginMember(String userPE, String password){
		JsonObject requestInfo = new JsonObject();
		requestInfo.addProperty("user_pe", userPE);
		requestInfo.addProperty("password", password);
		try {
			requestInfo.addProperty("login_ip", NetworkInfoUtil.getRealIp());
			requestInfo.addProperty("login_mac", NetworkInfoUtil.getLocalMac());
			requestInfo.addProperty("login_client_type", 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
		
		Object object = HTTPUtil.request(Res.Url(Res.Login_Servlet), rsaInfo);
		
		if(object==null)
			return;
		
		String responseStr = (String) object;
		JsonObject responseInfo = (JsonObject) new JsonParser().parse(responseStr);
		int result_code = responseInfo.get("result_code").getAsInt();
//		String cache = responseInfo.get("login_cache").getAsString();
		System.err.println("CODE : " + result_code);
//		System.err.println("CACHE : " + cache);
		switch(result_code){
		case 0:
			new Thread(){public void run() {
				STLoadDialog loadDialog = new STLoadDialog("初始化", PandaOfficeLoginGUI.this);
				loadDialog.showDialog();
				MemberInfo memberInfo = new MemberInfo();
				memberInfo.setUserName("李佳骏");
				memberInfo.setUserIconFileId("");
				MainFrame frame = new MainFrame(memberInfo);
				loadDialog.setVisible(false);
				frame.setVisible(true);
				setVisible(false);
			};}.start();
			return;
		case 1:
			JOptionPane.showMessageDialog(this, "此次为异地登陆,请注意账户安全!");
			new Thread(){public void run() {
				STLoadDialog loadDialog = new STLoadDialog("初始化", PandaOfficeLoginGUI.this);
				loadDialog.showDialog();
				MemberInfo memberInfo = new MemberInfo();
				memberInfo.setUserName("李佳骏");
				memberInfo.setUserIconFileId("");
				MainFrame frame = new MainFrame(memberInfo);
				loadDialog.setVisible(false);
				frame.setVisible(true);
				setVisible(false);
			};}.start();
			return;
		case 2:
			JOptionPane.showMessageDialog(this, "用户不存在!");
			return;
		case 3:
			JOptionPane.showMessageDialog(this, "密码错误!");
			return;
		case 4:
			JOptionPane.showMessageDialog(this, "系统拒绝登陆!");
			return;
		}
		
	}
	
	public boolean registerMember(String captche){
		String name = regTexts[0].getText();
		String phoneNumber = regTexts[1].getText();
		String password = new String(((JPasswordField)regTexts[2]).getPassword());
		
		JsonObject requestInfo = new JsonObject();
		requestInfo.addProperty("captche", captche);
		requestInfo.addProperty("name", name);
		requestInfo.addProperty("phone_number", phoneNumber);
		requestInfo.addProperty("password", password);
		
		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
		
		Object object = HTTPUtil.request(Res.Url(Res.Register_Servlet), rsaInfo);
		if(object==null)
			return false;
		String responseStr = (String) object;
		JsonObject responseInfo = (JsonObject) new JsonParser().parse(responseStr);
		int result_code = responseInfo.get("result_code").getAsInt();
		switch (result_code) {
		case Member.MEMBER_EXISTS:
			JOptionPane.showMessageDialog(null, "手机号码已被注册!", "警告", JOptionPane.WARNING_MESSAGE);
			return false;
		case 3:
			JOptionPane.showMessageDialog(null, "成功了诶");
			return true;
		}
		
		return false;
	}
	
	public boolean requestCaptche(){
		
		String phoneNumber = regTexts[1].getText();
		
		JsonObject requestInfo = new JsonObject();
		requestInfo.addProperty("captche_type", Captche.CAPTCHE_TYPE_REG_MEMBER);
		requestInfo.addProperty("phone_number", phoneNumber);
		
		RSAInfo rsaInfo = Panda_RSA_Algorithm.buildRSAInfo(requestInfo);
		
		Object object = HTTPUtil.request(Res.Url(Res.Captche_Servlet), rsaInfo);
		
		if(object==null)
			return false;
		
		String responseStr = (String) object;
		JsonObject responseInfo = (JsonObject) new JsonParser().parse(responseStr);
		int result_code = responseInfo.get("result_code").getAsInt();
		switch (result_code) {
		case Member.MEMBER_EXISTS:
			JOptionPane.showMessageDialog(null, "手机号码已被注册!", "警告", JOptionPane.WARNING_MESSAGE);
			return false;
		case Captche.CAPTCHE_REQUEST_OK:
			return true;
		}
		
		return false;
		
	}
	
	public boolean validateRegTexts(){
		for(JTextComponent textComponent:regTexts){
			if(textComponent.getText().isEmpty()){
				JOptionPane.showMessageDialog(null, "请完整填写所有的表单项目！", "警告", JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}
		if(regTexts[1].getText().length()!=11){
			JOptionPane.showMessageDialog(null, "手机号码长度有误！", "警告", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if(new String(((JPasswordField)regTexts[2]).getPassword()).length()<6){
			JOptionPane.showMessageDialog(null, "密码不能小于6位！", "警告", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

}
