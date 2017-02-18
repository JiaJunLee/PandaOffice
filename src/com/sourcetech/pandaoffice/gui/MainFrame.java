package com.sourcetech.pandaoffice.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sourcetech.pandaoffice.dao.Member;
import com.sourcetech.pandaoffice.dao.MemberInfo;
import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STFrame;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.util.FontUtil;
import com.sourcetech.pandaoffice.util.MMCQ;
import com.sourcetech.pandaoffice.util.UIFactory;
import com.sourcetech.pandaoffice.util.UIFactory.DefaultTextField;
import com.sourcetech.pandaoffice.view.ImageButtonGroup;
import com.sourcetech.pandaoffice.view.Tween;
import com.sourcetech.pandaoffice.view.ImageButtonGroup.ButtonGroupListener;
import com.sourcetech.pandaoffice.view.ViewPager;
import com.sourcetech.pandaoffice.widget.STImageButton;
import com.sourcetech.pandaoffice.widget.STImageView;
import com.sourcetech.pandaoffice.widget.STTimeView;
import com.sourcetech.pandaoffice.widget.STUserIcon;
import com.sourcetech.pandaoffice.widget.STWorkTitle;
import com.sourcetech.pandaoffice.widget.STSkinChooser;
import com.sourcetech.pandaoffice.widget.STSkinChooser.Background;

public class MainFrame extends STFrame {

	/**
	 * Pane初始化数量
	 */
	public static final int Pane_Count = 3;

	/**
	 * Frame尺寸
	 */
	public static final int Frame_Width = 1330;
	public static final int Frame_Height = 762;

	/**
	 * 内边距
	 */
	public static final int offset_X = 15;
	public static final int offset_Y = 15;
	
	/**
	 * 用户头像大小
	 */
	public static final int ICON_SIZE = 54;
	
	public static final Font DEFAULT_TEXT_FONT = FontUtil.getFont(Res.Font(Res.FZ_LTH), 0, 13f);

	// MainFrame 中的所有 pane 集合
	private JLayeredPane[] panes;
	// MainFrame 背景层
	private STImageView background;
	// nav button group
	private ImageButtonGroup buttonGroup;
	// 系统时钟
	private STTimeView timeView;
	// 用户信息
	private MemberInfo memberInfo;

	// 背景选择Dialog
	private STSkinChooser ST_SKIN_CHOOSER;
	// 业务Pane ViewPager
	private ViewPager worksViewPager;
	// 业务Panes
	private ArrayList<STPanel> viewPanels;

	public MainFrame(MemberInfo memberInfo) {
		super(Pane_Count, Frame_Width, Frame_Height);
		setDefaultCloseOperation(STFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		this.memberInfo = memberInfo;

		panes = getPanes();

		// 初始化背景层
		background = new STImageView();
		background.setBounds(0, 0, Frame_Width, Frame_Height);
		panes[0].add(background);

		// 初始化顶部导航背景
		STImageView topBackground = new STImageView(Res.Main_Frame_Top_Background);
		topBackground.setBounds(offset_X, offset_Y, Frame_Width - 2 * offset_X, 80);
		panes[1].add(topBackground);

		// 初始化LOGO
		STImageView logo = new STImageView(Res.Main_Frame_Logo);
		logo.setBounds(34 + offset_X, 17 + offset_Y, 177, 47);
		panes[2].add(logo);
 
		// 初始化时钟控件
		timeView = new STTimeView(STColor.ALPHA);
		timeView.setBounds(130, 310);
		panes[2].add(timeView);

		// 初始化背景选择控件,并选择默认背景
		ST_SKIN_CHOOSER = new STSkinChooser(this);

		// 初始化导航
		initNavs();
		// 初始化功能按钮
		initDefaultButtons();
		// 初始化用户头像和数据
		initUserData();
		
		// 初始化业务面板
		initWorkPanes();
	}
	
	public static final int Work_Pane_Width = 1040;
	public static final int Work_Pane_Height = 585;

	private void initWorkPanes() {
		worksViewPager = ViewPager
				.newBuilder()
				.setBounds(
						new Rectangle((Frame_Width - Work_Pane_Width) / 2, 114 + offset_Y, 1040, Work_Pane_Height))
				.setPageCount(2).setSpeed(15)
				.setTween(Tween.Function_Cubic, Tween.Type_Easy_In).build();

		// 获取ViewPage的页面
		viewPanels = worksViewPager.getPanes();

		Member member = new Member();
		member.setUserId(4);
		ProjectPane projectPane = new ProjectPane(member);
		projectPane.setBounds(0, 0,Work_Pane_Width,Work_Pane_Height);
		
		viewPanels.get(0).add(projectPane);
		
		STPanel panel2 = new STPanel(new File(Res.Mipmap(Res.Main_Frame_Work_Pane_Background)));
		panel2.setBounds(0, 0, Work_Pane_Width, Work_Pane_Height);
		viewPanels.get(1).add(panel2);
		
		panes[2].add(worksViewPager);
		worksViewPager.setSelectedPage(-1);
	}

	public void setBackground(Background background) {
		this.background.setIcon(background.image);
		timeView.setTextColor(background.textColor);
	}

	private void initUserData() {
		// 用户头像
		STUserIcon icon = new STUserIcon(ICON_SIZE, new File(Res.Mipmap(Res.User_Icon)));
		icon.setBounds(1111 + offset_X, 14 + offset_Y, ICON_SIZE, ICON_SIZE);
		panes[2].add(icon);
		// 用户信息
		DefaultTextField nameText = new DefaultTextField("亲爱的  " + memberInfo.getUserName(), DEFAULT_TEXT_FONT, STColor.GRAY, false);
		nameText.autoSize();
		nameText.setBounds(1092 + offset_X - nameText.getWidth(), 24 + offset_Y, nameText.getWidth(), nameText.getHeight());
		panes[2].add(nameText);
		// 问候
		DefaultTextField tipText = new DefaultTextField("下午好", DEFAULT_TEXT_FONT, STColor.GRAY, false);
		tipText.autoSize();
		tipText.setBounds(1092 + offset_X - tipText.getWidth(), 46 + offset_Y, tipText.getWidth(), tipText.getHeight());
		panes[2].add(tipText);
	}

	private void initDefaultButtons() {
		String[] icons = new String[] { Res.Mipmap(Res.Deafult_Style_btn), Res.Mipmap(Res.Deafult_Minimality_btn),
				Res.Mipmap(Res.Deafult_Close_btn) };

		int x = 1184 + offset_X;
		int y = 30 + offset_Y;
		STImageButton[] buttons = new STImageButton[3];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = STImageButton.newBuilder().setIcon(icons[i]).autoSize(x, y).build();
			panes[2].add(buttons[i]);
			x += 36;
		}
		buttons[0].addButtonListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Point point = getLocation();
				ST_SKIN_CHOOSER.setLocation(point.x + 994, point.y + 78);
				ST_SKIN_CHOOSER.setVisible(true);
			}
		});
		buttons[2].addButtonListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
	}

	private void initNavs() {
		String[] icons = new String[] { Res.Mipmap(Res.Nav_Item_Btn), Res.Mipmap(Res.Nav_Company_Btn),
				Res.Mipmap(Res.Nav_My_Btn), Res.Mipmap(Res.Nav_Calendar_Btn), Res.Mipmap(Res.Nav_Message_Btn) };
		String[] navTexts = new String[] { Res.Text(Res.Nav_Item_text), Res.Text(Res.Nav_Company_text),
				Res.Text(Res.Nav_My_text), Res.Text(Res.Nav_Calendar_text), Res.Text(Res.Nav_Message_text) };
		buttonGroup = new ImageButtonGroup();
		buttonGroup.setBounds(345 + offset_X, 12 + offset_Y);
		buttonGroup.setButtonMargin(48);

		for (int i = 0; i < icons.length; i++) {
			STImageButton button = STImageButton.newBuilder().setIcon(icons[i]).setText(navTexts[i]).build();
			buttonGroup.addButton(button);
		}
		
		buttonGroup.addSelectListener(new ButtonGroupListener() {
			public void OnSelected(int index) {
				worksViewPager.setSelectedPage(index);
				timeView.setVisible(false);
			}
			public void OnMetaDown(int index) {
			}
		});
		panes[2].add(buttonGroup);
	}

	public static void main(String[] args) {
		MemberInfo memberInfo = new MemberInfo();
		memberInfo.setUserName("李佳骏");
		memberInfo.setUserIconFileId("");
		MainFrame frame = new MainFrame(memberInfo);
		frame.setVisible(true);
	}

}
