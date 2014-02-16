package com.suchbeacon.web.servlet;

import javax.servlet.http.HttpServlet;

import com.googlecode.objectify.ObjectifyService;
import com.suchbeacon.web.Content;
import com.suchbeacon.web.User;

public class StartupServlet extends HttpServlet{
	static {
    ObjectifyService.register(Content.class);
    ObjectifyService.register(User.class);
	}
}
