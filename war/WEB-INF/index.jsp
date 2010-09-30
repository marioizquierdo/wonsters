<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/Struts/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/views/layouts/front_page_ayout.jsp" flush="true">
<put name="title" value="FrontPage.title" />
		<tiles:put name="color" value="blue" /> <!-- values: green, blue, red, purple, yellow, grey -->
		<tiles:put name="footer" value="/views/layouts/front_page_footer.jspx" />
		<tiles:put name="title" value="FrontPage.title" />
		<tiles:put name="content" value="/views/front_page/game_presentation.jspx" />
		<tiles:put name="frontPageWindow_selected_tag" value="start" />
</tiles:insert>