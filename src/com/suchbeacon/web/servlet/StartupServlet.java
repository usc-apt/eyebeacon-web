package com.suchbeacon.web.servlet;

import javax.servlet.http.HttpServlet;

import com.googlecode.objectify.ObjectifyService;
import com.suchbeacon.web.Content;

public class StartupServlet extends HttpServlet{
	static {
    ObjectifyService.register(Content.class);
	}
}
