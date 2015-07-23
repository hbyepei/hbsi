<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="image/jpeg" import="java.awt.*,java.awt.image.*,javax.imageio.*,whu.b606.util.CommonUtils,whu.b606.util.ImageUtil"%>
<%
	response.setHeader("Pragma", "No-cache");//设置页面不缓存
	response.setHeader("Cache-Control", "no-cache"); 
	response.setDateHeader("Expires", 0);
	//下面的代码用以获得专家用户的图像的物理路径
	String fileName = request.getParameter("imagePath");
	String savePath = CommonUtils.getParameter("config/commonData", "expertImageSavePath");//相对于项目的根路径
	String realpath = CommonUtils.getProjectWebRoot() +savePath+ "/" + fileName;//专家图片文件的磁盘路径
	String ext = realpath.substring(realpath.lastIndexOf(".") + 1).toUpperCase();
	try {
		BufferedImage origin_img = ImageUtil.loadImageLocal(realpath);
		if(origin_img==null){//未加载成功
			origin_img=ImageUtil.loadImageLocal(CommonUtils.getProjectWebRoot()+"/images/expert/default.png");
			ext="PNG";
		}
		Graphics2D g=origin_img.createGraphics();
		BufferedImage new_img=ImageUtil.resize(origin_img, 28, 28, false);
		g.dispose();
		ImageIO.write(new_img, ext, response.getOutputStream());// 输出图象到页面
		out.clear();
		out = pageContext.pushBody();
	} catch (Exception e) {
		e.printStackTrace();
	}
%>