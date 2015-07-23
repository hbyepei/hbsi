<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="image/jpeg" import="java.awt.*,java.awt.image.*,javax.imageio.*,whu.b606.util.CommonUtils,whu.b606.util.ImageUtil"%>
<%
	response.setHeader("Pragma", "No-cache");//设置页面不缓存
	response.setHeader("Cache-Control", "no-cache"); 
	response.setDateHeader("Expires", 0);
	//根据参数选择背景图片并得到其路径
	String bgImg = "";
	String picName = "unknow.png";
	String str = request.getParameter("rank");
	int rank = -1;
	if (str != null && !str.equals("")) {//如果有正常的排名
		try {
			rank = Integer.parseInt(str);
			if (rank > 0 && rank < 4) {
				picName = "rank" + rank + ".png";
			} else if (rank > 3) {
				picName = "rank4.png";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	bgImg = CommonUtils.getProjectWebRoot() + "/images/expert/" + picName;//得到了排名使用的背景图片
	try {
		BufferedImage image = ImageUtil.loadImageLocal(bgImg);
		Graphics g = image.getGraphics();
		g.setColor(new Color(3, 3, 3));
		g.setFont(new Font("serif", Font.CENTER_BASELINE, 16));
		int xPoint = 10;
		if (rank > 9 && rank < 100) {
			xPoint = 6;
		} else if (rank > 99) {
			xPoint = 2;
		}
		g.drawString("" + rank, xPoint, 18);
		g.dispose(); // 图象生效
		ImageIO.write(image, "PNG", response.getOutputStream());// 输出图象到页面
		out.clear();
		out = pageContext.pushBody();
	} catch (Exception e) {
		e.printStackTrace();
	}
%>