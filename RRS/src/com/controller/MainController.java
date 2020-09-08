package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.frame.Biz;
import com.vo.MemberVO;
import com.vo.ShopVO;
import com.vo.Shop_commentVO;


@Controller
public class MainController {

	@Resource(name="mbiz")
	Biz<String, MemberVO> mbiz;

	@Resource(name="cbiz")
	Biz<Integer, Shop_commentVO> cbiz;

	@RequestMapping("/main.mc")
	public ModelAndView main() {
		ModelAndView mv = new ModelAndView();

		//��� ����
		ArrayList<Shop_commentVO> shop_comment = null;

		try {
			shop_comment = cbiz.get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		mv.addObject("shop_comment", shop_comment);
		mv.setViewName("main");
		return mv;
	}

	//�α��� �������� �̵�
	@RequestMapping("/login.mc")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView();
//		mv.addObject("centerpage", "login");
		mv.setViewName("login");
		return mv;
	}

	//�α��� ����
	@RequestMapping("/loginimpl.mc")                 //alert ����� ���� HttpServletResponse response �߰�
	public ModelAndView loginimpl(HttpServletRequest request, HttpServletResponse response) throws IOException {

		ModelAndView mv = new ModelAndView();
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		MemberVO dbuser = null;

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		try {
			dbuser = mbiz.get(id);
			if(dbuser.getUserpwd().equals(pwd)) {
				HttpSession session = request.getSession();
				session.setAttribute("loginuser", dbuser);
				mv.addObject("centerpage", "first");
			}else { //password ���� ��
				mv.addObject("centerpage", "login");
				//spring���� alert�� �ϴ� ����----------------------------------

				out.println("<script>alert('�α��ο� �����Ͽ����ϴ�'); </script>");
				out.flush();
				//---------------------------------------------------------
			}
		} catch (Exception e) {
			mv.addObject("centerpage", "login");
			out.println("<script>alert('�α��� �����Դϴ�'); </script>");
			out.flush();
			e.printStackTrace();
		}
		mv.setViewName("main");
		return mv;
	}

	@RequestMapping("/logout.mc")
	public ModelAndView logout(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		HttpSession session = request.getSession();
		if(session != null) {
			session.invalidate();
		}
		mv.setViewName("main");
		return mv;
	}


	@RequestMapping("/join.mc")
	public ModelAndView useradd(ModelAndView mv) {
	//	mv.addObject("centerpage", "member/join");
		mv.setViewName("member/join");
		return mv;
	}

	//ȸ������ ����
	@RequestMapping("/joinimpl.mc")
	public ModelAndView useraddimpl(ModelAndView mv, MemberVO member,HttpServletResponse response) throws IOException {

		try {
			mbiz.register(member);
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('ȸ������ �Ǿ����ϴ�'); </script>");
			out.flush();
			mv.addObject("centerpage", "first");
		} catch (Exception e) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('ȸ������ �����Դϴ�'); </script>");
			out.flush();
			mv.addObject("centerpage", "member/join");
			e.printStackTrace();
		}
		mv.setViewName("main");
		return mv;
	}


	//========================����=============================

	public String getRequest(String url, String parameter) {

		try {
			String param = "{\"param\":\"" + parameter + "\"} ";

			// url�� �ν��Ͻ��� �����.
			URL uri = new URL(url);
			// HttpURLConnection �ν��Ͻ��� �����´�.
			HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
			// Web Method�� Post Ÿ��
			connection.setRequestMethod("GET");

			// ��û�Ѵ�. 200�̸� �����̴�.
			int code = connection.getResponseCode();
			if (code == 200) {

				// ���� �� body ������ stream�� �޴´�.
				try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
					String line;
					StringBuffer buffer = new StringBuffer();
					while ((line = input.readLine()) != null) {
						buffer.append(line);
					}

					return buffer.toString();
				}
			}

			return code + "";
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}


	@RequestMapping("/getweather.mc")
	public void getweather(HttpServletRequest request, HttpServletResponse response) throws IOException {

		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMdd");
		SimpleDateFormat format2 = new SimpleDateFormat ( "HH00" );

		Date time = new Date();

		String timeDate = format1.format(time);
		String timeHour = format2.format(time);
		//����ð����� ���� �� ���� ���޾ƿ��� ������ ����ð����� 1�ð� �� �ð����� ����
		int a = Integer.parseInt(timeHour);
		if(a==100) {
			timeHour="0000";
		}else if( a==0) {
			timeHour="2300";
		}else if(a<1000) {
			a = a-100;
			timeHour = "0"+Integer.toString(a);
		}else {
			a = a-100;
			timeHour = Integer.toString(a);
		}

		String urlstr = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst?serviceKey=GCKdLak3diVhCnTIjF1tEy7Jg4%2B%2BCZjtNKfXmQqAbkAYipPxWX%2Fv1mvxvUsDA6%2By9lyZDj%2FP0h%2FiCs36Mi46eg%3D%3D&pageNo=1&numOfRows=10&dataType=XML&base_date="+timeDate+"&base_time="+timeHour+"&nx=60&ny=127&";
		String xmlstr = getRequest(urlstr,"");
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(xmlstr);

	}




}




