package com.weibo.lodil;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;

public class Main {

	public static void main(final String[] args) throws Exception {
		final Server server = new Server(8080);
		server.start();

	}

	public static class TestServlet extends HttpServlet {

		@Override
		public void init(final ServletConfig servletConfig) throws ServletException {

		}

		@Override
		protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

		}

		@Override
		protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

		}
	}

}
