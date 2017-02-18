package com.sourcetech.pandaoffice.detail;

import java.net.MalformedURLException;
import java.net.URL;

import com.sourcetech.pandaoffice.detail.Res.MipmapRes;

public class Res {
	
	public static class MipmapRes extends Res{ public MipmapRes(String res) {super(res);}}
	public static class FontRes extends Res{ public FontRes(String res) {super(res);}}
	public static class TextRes extends Res{ public TextRes(String res) {super(res);}}
	public static class DicRes extends Res{ public DicRes(String res) {super(res);}}
	public static class ConfigRes extends Res{ public ConfigRes(String res) {super(res);}}
	public static class URLRes extends Res{ public URLRes(String res) {super(res);}}
	
	public static final String rootPath = System.getProperty("user.dir");
	public static final String mipmapPath = "\\res\\mipmap";
	public static final String fontPath = "\\res\\font";
	public static final String configPath = "\\res\\config";
	public static final String serverPath = "http://localhost:8080/PandaOfficeServer/";
	
	private String res;
	public Res(String res){
		this.res = res;
	}
	public String getRes(){return res;}

	public static String Mipmap(MipmapRes res){
		return rootPath + mipmapPath + res.getRes();
	}
	
	public static String Font(FontRes res){
		return rootPath + fontPath + res.getRes();
	}
	
	public static String Text(TextRes res){
		return res.getRes();
	}
	
	public static String Dic(DicRes res){
		return rootPath + res.getRes();
	}
	
	public static String Config(ConfigRes res){
		return rootPath + configPath + res.getRes();
	}
	
	public static URL Url(URLRes res){
		try {
			return new URL(serverPath + res.getRes());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static final MipmapRes STPanel_Default_Background = new MipmapRes("\\default_background.9.png");
	public static final MipmapRes ST_Skin_Chooser_Background = new MipmapRes("\\st_skin_chooser_background.9.png");
	public static final MipmapRes TextView_Default_Background = new MipmapRes("\\textview_bg.9.png");
	public static final MipmapRes Panda_Office_Logo = new MipmapRes("\\logo.png");
	
	public static final MipmapRes Main_Frame_Top_Background = new MipmapRes("\\main\\top_background.png");
	public static final MipmapRes Main_Frame_Logo = new MipmapRes("\\main\\logo_small.png");
	public static final MipmapRes Main_Frame_Work_Pane_Background = new MipmapRes("\\main\\work_pane_background.9.png");
	
	public static final MipmapRes Nav_Item_Btn = new MipmapRes("\\main\\top_nav_buttons\\item_btn_default.png");
	public static final MipmapRes Nav_Company_Btn = new MipmapRes("\\main\\top_nav_buttons\\company_btn_default.png");
	public static final MipmapRes Nav_My_Btn = new MipmapRes("\\main\\top_nav_buttons\\my_btn_default.png");
	public static final MipmapRes Nav_Calendar_Btn = new MipmapRes("\\main\\top_nav_buttons\\calendar_btn_default.png");
	public static final MipmapRes Nav_Message_Btn = new MipmapRes("\\main\\top_nav_buttons\\message_btn_default.png");
	
	public static final MipmapRes Deafult_Close_btn = new MipmapRes("\\main\\default_buttons\\close.png");
	public static final MipmapRes Deafult_Minimality_btn = new MipmapRes("\\main\\default_buttons\\minimality.png");
	public static final MipmapRes Deafult_Style_btn = new MipmapRes("\\main\\default_buttons\\style.png");
	
	public static final MipmapRes Project_Detail_Back_btn = new MipmapRes("\\project_detail\\btn_back.png");
	public static final MipmapRes TaskGroup_Detail_Back_btn = new MipmapRes("\\project_detail\\btn_detail.png");
	public static final MipmapRes Task_List_Background = new MipmapRes("\\project_detail\\list_bg.9.png");
	
	public static final MipmapRes File_Bg_Background = new MipmapRes("\\file_detail\\file_bg.png");
	public static final MipmapRes Upload_Btn = new MipmapRes("\\file_detail\\btn_upload.png");
	
	public static final MipmapRes User_Icon = new MipmapRes("\\main\\usericon.jpg");
	
	public static final MipmapRes Image_Util_Blank_Background = new MipmapRes("\\blank_background.png");
	public static final MipmapRes Image_Util_Project_View_Shadow = new MipmapRes("\\main\\project_view\\background_shadow.png");
	
	public static final MipmapRes ST_Skin_Chooser_Add_Btn = new MipmapRes("\\main\\st_skin_chooser\\add_btn.png");
	
	public static final MipmapRes Create_Project_Img = new MipmapRes("\\project_detail\\create_project.png");
	public static final MipmapRes Create_Project_Dialog_Img = new MipmapRes("\\project_detail\\create_project_dialog.png");
	public static final MipmapRes Create_Task_btn = new MipmapRes("\\project_detail\\create_task.png");
	
	public static final FontRes FZ_LTH = new FontRes("\\LTH.TTF");
	public static final FontRes HAGIN_CAPS = new FontRes("\\HaginCaps.otf");
	
	public static final TextRes Login_Tab_View_login_text = new TextRes("登录");
	public static final TextRes Login_Tab_View_Register_text = new TextRes("注册");
	public static final TextRes Login_Button_Login_text = new TextRes("登 录");
	public static final TextRes Login_Button_Register_text = new TextRes("注 册");
	
	public static final TextRes Nav_Item_text = new TextRes("项 目");
	public static final TextRes Nav_Company_text = new TextRes("企 业");
	public static final TextRes Nav_My_text = new TextRes("我 的");
	public static final TextRes Nav_Calendar_text = new TextRes("日 历");
	public static final TextRes Nav_Message_text = new TextRes("通 知");
	
	public static final DicRes Main_Frame_Backgrounds = new DicRes("\\res\\mipmap\\main\\backgrounds");
	public static final DicRes Main_Frame_User_Backgrounds = new DicRes("\\res\\mipmap\\main\\backgrounds\\user");
	public static final DicRes Thumb_Project_Image_Dic = new DicRes("\\res\\mipmap\\thumb\\project_img");
	public static final DicRes File_Detail_Dic = new DicRes("\\res\\mipmap\\file_detail");
	
	
	public static final ConfigRes Main_Frame_Config = new ConfigRes("\\mainframe_config.properties");
	public static final ConfigRes Client_Encrypt_Config = new ConfigRes("\\client_encrypt.config");
	
	public static final URLRes Captche_Servlet = new URLRes("captche.request");
	public static final URLRes Register_Servlet = new URLRes("register.do");
	public static final URLRes Login_Servlet = new URLRes("login.do");
	public static final URLRes Project_Servlet = new URLRes("project");
	public static final URLRes Download_Servlet = new URLRes("down");
	public static final URLRes TaskGroup_Servlet = new URLRes("taskgroup");
	public static final URLRes TaskList_Servlet = new URLRes("tasklist");
	public static final URLRes Task_Servlet = new URLRes("task");
	public static final URLRes File_Servlet = new URLRes("File");
	public static final URLRes Upload_Servlet = new URLRes("upload");
	public static final URLRes FileRelate_Servlet = new URLRes("file_relate");

}
