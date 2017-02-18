package com.sourcetech.pandaoffice.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.sourcetech.pandaoffice.detail.Res;
import com.sourcetech.pandaoffice.detail.Res.DicRes;
import com.sourcetech.pandaoffice.detail.STColor;
import com.sourcetech.pandaoffice.frame.STDialog;
import com.sourcetech.pandaoffice.frame.STFrame;
import com.sourcetech.pandaoffice.frame.STGridPane;
import com.sourcetech.pandaoffice.frame.STPanel;
import com.sourcetech.pandaoffice.gui.MainFrame;
import com.sourcetech.pandaoffice.util.FileUtil;
import com.sourcetech.pandaoffice.util.ImageUtil;
import com.sourcetech.pandaoffice.util.MMCQ;
import com.sourcetech.pandaoffice.util.UIFactory.DefaultTextField;
import com.sourcetech.pandaoffice.view.ImageButtonGroup;
import com.sourcetech.pandaoffice.view.ImageButtonGroup.ButtonGroupListener;
import com.sourcetech.pandaoffice.widget.STSkinChooser.BackgroundGroup;
import com.sourcetech.pandaoffice.widget.ScrollTest.MyScrollBar;

public class STSkinChooser extends STDialog {

	private static final int DEFAULT_BACKGROUND = 0;
	private static final int USER_BACKGROUND = 1;

	private static final int CHOOSER_WIDTH = 300;
	private static final int CHOOSER_HEIGHT = 235;

	private static final int SCROLL_PANE_WIDTH = 283;
	private static final int SCROLL_PANE_HEIGHT = 175;

	private static final int CHOOSER_PANE_COUNT = 2;

	private ArrayList<Background> defaultBackgrounds = initBackgrounds(Res.Main_Frame_Backgrounds);
	private ArrayList<Background> userBackgrounds = initBackgrounds(Res.Main_Frame_User_Backgrounds);
	private MainFrame frame; // Parent Frame
	private JLayeredPane[] panes; // 控件层
	private JScrollPane scrollPanel;
	private STPanel contentPane; // ScrollPane内部Pane
	private STGridPane defaultGridPane; // 装载系统默认壁纸网格布局
	private STGridPane userGridPane; // 装载用户壁纸网格布局
	private DefaultTextField[] tipTexts = new DefaultTextField[2]; // 提示文字
	private BackgroundGroup defaultGroup;
	private BackgroundGroup userGroup;

	public static class Background {
		public BufferedImage image;
		public Color textColor;
		public File file;
	}

	public static class BackgroundGroup {
		private ArrayList<STImageButton> backgrounds = new ArrayList<STImageButton>();
		private ButtonGroupListener listener;
		private int currentIndex = -1;

		public void addListener(ButtonGroupListener listener) {
			this.listener = listener;
		}

		public void remove(int index) {
			backgrounds.remove(index);
		}
		
		public int getSelect(){
			return currentIndex;
		}

		public void setSelect(int index) {
			if (currentIndex != index) {
				if (listener != null) {
					listener.OnSelected(index);
				}
				currentIndex = index;
			}
		}

		public void add(final STImageButton background) {
			backgrounds.add(background);
			background.addButtonListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					int index = backgrounds.indexOf(background);
					if (e.isMetaDown()) {
						if (listener != null)
							listener.OnMetaDown(index);
						return;
					}
					setSelect(index);
				}
			});
		}
	}

	private static Color getTextColor(BufferedImage image) {
		List<int[]> result = null;
		try {
			result = MMCQ.compute(image, 10);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return STColor.isDeepColor(result.get(0)) ? STColor.WHITE : STColor.decode("#676767");
	}

	private static ArrayList<Background> initBackgrounds(DicRes res) {
		ArrayList<String> backgroundPaths = (ArrayList<String>) FileUtil.getFileList(Res.Dic(res));
		ArrayList<Background> backgrounds = new ArrayList<STSkinChooser.Background>();
		for (String backgroundPath : backgroundPaths) {
			try {
				File file = new File(backgroundPath);
				BufferedImage bufferedImage = ImageIO.read(file);
				bufferedImage = ImageUtil.getBackgroundImage(bufferedImage);
				Background background = new Background();
				background.image = bufferedImage;
				background.textColor = getTextColor(bufferedImage);
				background.file = file;
				backgrounds.add(background);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return backgrounds;
	}

	private void initScrollPane() {

		contentPane = new STPanel(true);

		scrollPanel = new JScrollPane(contentPane);
		scrollPanel.setBorder(null);
		scrollPanel.setBackground(STColor.ALPHA);
		scrollPanel.setBounds(10, 15, SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT);
		scrollPanel.getVerticalScrollBar().setUnitIncrement(50);
		scrollPanel.getVerticalScrollBar().setBackground(STColor.ALPHA);
		scrollPanel.getVerticalScrollBar().setPreferredSize(new Dimension(8, 175));
		scrollPanel.getVerticalScrollBar().setUI(new MyScrollBar());
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		panes[1].add(scrollPanel);
	}

	private void addUserBackground(File file) {
		try {

			Background background = new Background();
			background.image = ImageUtil.getBackgroundImage(ImageIO.read(file));
			background.textColor = getTextColor(background.image);
			background.file = file;

			userBackgrounds.add(background);

			BufferedImage bufferedImage = background.image;
			bufferedImage = (BufferedImage) ImageUtil.getScaledImage(bufferedImage, 90, 1);
			STImageButton imageButton = STImageButton.newBuilder().setIcon(bufferedImage).setShowShadow(true)
					.autoSize(0, 0).build();

			userGroup.add(imageButton);
			userGridPane.add(imageButton);
			userGridPane.autoSize();
			autoSize();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initDefaultGirdPane() {

		tipTexts[0] = new DefaultTextField("--------------系统壁纸--------------", STImageButton.DEFAULT_TEXT_FONT,
				STImageButton.DEFAULT_TEXT_COLOR, false);
		tipTexts[0].autoSize();

		defaultGridPane = new STGridPane(3, 2, 2, true);

		defaultGroup = new BackgroundGroup();

		for (int i = 0; i < defaultBackgrounds.size(); i++) {
			BufferedImage bufferedImage = defaultBackgrounds.get(i).image;
			bufferedImage = (BufferedImage) ImageUtil.getScaledImage(bufferedImage, 90, 1);
			STImageButton imageButton = STImageButton.newBuilder().setIcon(bufferedImage).setShowShadow(true)
					.autoSize(0, 0).build();
			defaultGroup.add(imageButton);
			defaultGridPane.add(imageButton);
		}

		defaultGroup.addListener(new ButtonGroupListener() {
			public void OnSelected(int index) {
				frame.setBackground(defaultBackgrounds.get(index));
				updateProperties(index, DEFAULT_BACKGROUND);
				setVisible(false);
			}

			public void OnMetaDown(int index) {
				JOptionPane.showMessageDialog(frame, "自带壁纸不可删除！", "警告", JOptionPane.WARNING_MESSAGE);
			}
		});

		defaultGridPane.autoSize();

		contentPane.add(tipTexts[0]);
		contentPane.add(defaultGridPane);

	}

	private void initUserGirdPane() {

		tipTexts[1] = new DefaultTextField("--------------用户壁纸--------------", STImageButton.DEFAULT_TEXT_FONT,
				STImageButton.DEFAULT_TEXT_COLOR, false);
		tipTexts[1].autoSize();

		userGridPane = new STGridPane(3, 2, 2, true);

		userGroup = new BackgroundGroup();

		for (int i = 0; i < userBackgrounds.size(); i++) {
			BufferedImage bufferedImage = userBackgrounds.get(i).image;
			bufferedImage = (BufferedImage) ImageUtil.getScaledImage(bufferedImage, 90, 1);
			STImageButton imageButton = STImageButton.newBuilder().setIcon(bufferedImage).setShowShadow(true)
					.autoSize(0, 0).build();
			userGroup.add(imageButton);
			userGridPane.add(imageButton);
		}

		userGroup.addListener(new ButtonGroupListener() {
			public void OnSelected(int index) {
				frame.setBackground(userBackgrounds.get(index));
				updateProperties(index, USER_BACKGROUND);
				setVisible(false);
			}

			public void OnMetaDown(int index) {
				if(userGroup.getSelect()==index){
					JOptionPane.showMessageDialog(frame, "不能删除当前壁纸!","警告",JOptionPane.WARNING_MESSAGE);
					return;
				}
				int result = JOptionPane.showConfirmDialog(frame, "是否删除此壁纸?", "询问", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					boolean deleteResult = userBackgrounds.get(index).file.delete();
					if (deleteResult) {
						userBackgrounds.remove(index);
						userGridPane.remove(index);
						userGridPane.autoSize();
						userGroup.remove(index);
						autoSize();
						JOptionPane.showMessageDialog(frame, "删除成功");
					}
				}
			}
		});

		userGridPane.autoSize();

		contentPane.add(tipTexts[1]);
		contentPane.add(userGridPane);
	}

	public void autoSize() {
		tipTexts[0].setBounds((defaultGridPane.getPreferredSize().width - tipTexts[0].getWidth()) / 2, 5,
				tipTexts[0].getWidth(), tipTexts[0].getHeight());
		tipTexts[1].setBounds((defaultGridPane.getPreferredSize().width - tipTexts[1].getWidth()) / 2,
				20 + tipTexts[0].getHeight() + defaultGridPane.getPreferredSize().height, tipTexts[1].getWidth(),
				tipTexts[1].getHeight());
		defaultGridPane.setBounds(0, tipTexts[0].getHeight() + 10, defaultGridPane.getPreferredSize().width,
				defaultGridPane.getPreferredSize().height);
		userGridPane.setBounds(0,
				25 + tipTexts[0].getHeight() + tipTexts[1].getHeight() + defaultGridPane.getPreferredSize().height,
				userGridPane.getPreferredSize().width, userGridPane.getPreferredSize().height);
		contentPane.setPreferredSize(new Dimension(defaultGridPane.getPreferredSize().width,
				25 + tipTexts[0].getHeight() + tipTexts[1].getHeight() + defaultGridPane.getPreferredSize().height
						+ userGridPane.getPreferredSize().height));
		scrollPanel.repaint();
	}

	private void initAddButton() {
		STImageButton addButton = STImageButton.newBuilder().setIcon(Res.Mipmap(Res.ST_Skin_Chooser_Add_Btn))
				.setShowShadow(true).setText("添加自定义皮肤").setLayout(STImageButton.HORIZONAL).setButtonPadding(5)
				.setTextMargin(10).autoSize(88, 195).build();

		addButton.addButtonListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				ShowFileChooserDialog();
			}
		});
		panes[1].add(addButton);
	}

	public STSkinChooser(final MainFrame frame) {

		super(CHOOSER_PANE_COUNT, CHOOSER_WIDTH, CHOOSER_HEIGHT, frame, false);

		this.frame = frame;
		panes = getPanes();

		initBackground(); // 初始化背景
		initScrollPane(); // 初始化ScrollPane
		initDefaultGirdPane(); // 初始化系统默认壁纸网格布局
		initUserGirdPane(); // 初始化用户壁纸网格布局
		initAddButton(); // 用户添加自定义壁纸按钮
		autoSize(); // 自动适应内容尺寸

		// 从配置文件读取背景选项并选择默认背景
		Properties properties = roadProperties();
		int index = Integer.valueOf(properties.getProperty("DEFAULT_BACKGROUND"));
		try {
			switch (Integer.valueOf(properties.getProperty("BACKGROUND_TYPE"))) {
			case DEFAULT_BACKGROUND:
				defaultGroup.setSelect(index);
				break;
			case USER_BACKGROUND:
				userGroup.setSelect(index);
				break;
			}
		} catch (IndexOutOfBoundsException e) {
			defaultGroup.setSelect(0);
		}

		this.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				System.out.println("SkinChooser 失去焦点");
				setVisible(false);
			}

			public void focusGained(FocusEvent e) {
				System.out.println("SkinChooser 获得焦点");
			}
		});

	}

	private void initBackground() {
		STPanel bgPanel = new STPanel(new File(Res.Mipmap(Res.ST_Skin_Chooser_Background)));
		bgPanel.setBounds(0, 0, CHOOSER_WIDTH, CHOOSER_HEIGHT);
		panes[0].add(bgPanel);
	}

	private void ShowFileChooserDialog() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("图片文件", "jpg", "png");
		fileChooser.setFileFilter(filter);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnVal = fileChooser.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File newFile = saveBackground(fileChooser.getSelectedFile());
			addUserBackground(newFile);

			frame.setBackground(userBackgrounds.get(userBackgrounds.size() - 1));
			updateProperties(userBackgrounds.size() - 1, USER_BACKGROUND);
			userGroup.setSelect(userBackgrounds.size() - 1);

			setVisible(false);
		}
	}

	private File saveBackground(File file) {
		if (FileUtil.isImageFile(file)) {
			String newFileName = Res.Dic(Res.Main_Frame_User_Backgrounds) + "\\" + UUID.randomUUID() + "."
					+ FileUtil.getFileType(file);
			File newFile = new File(newFileName);
			FileUtil.TransferCopy(file, newFile);
			return newFile;
		}
		return null;
	}

	private Properties roadProperties() {
		try {
			Properties properties = new Properties();
			properties.load(new FileReader(new File(Res.Config(Res.Main_Frame_Config))));
			return properties;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void updateProperties(int defaultIndex, int type) {
		try {
			Properties properties = new Properties();
			properties.setProperty("DEFAULT_BACKGROUND", String.valueOf(defaultIndex));
			properties.setProperty("BACKGROUND_TYPE", String.valueOf(type));
			properties.store(new FileOutputStream(new File(Res.Config(Res.Main_Frame_Config))), "Skin Changed");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
