package com.controller;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.frame.Biz;
import com.frame.Find;
import com.vo.ShopVO;
import com.vo.Shop_commentVO;
import com.vo.Shop_recommendVO;

@Controller
public class ShopController {

	@Resource(name="sbiz")
	Biz<Integer, ShopVO> sbiz;

	@Resource(name="cbiz")
	Biz<Integer, Shop_commentVO> cbiz;

	//��� find�ϱ� ���� �������̽� �߰� ����
	@Resource(name="cbiz")
	Find<Integer, Shop_commentVO> fcomment;

	@Resource(name="rbiz")
	Biz<Integer, Shop_recommendVO> rbiz;

	//���� ��� �������� �̵�
	@RequestMapping("/shop_regist.mc")
	public ModelAndView shopadd(ModelAndView mv) {

		//mv.addObject("dbuser", member); //���̵� �ڵ� �Է��� ���� �����ü ��� 
		mv.addObject("centerpage", "shop/shop_regist");	//���� ���jsp
		mv.setViewName("main");
		return mv;

	}

	//���� ��� �Ϸ�
	@RequestMapping("/shop_registimpl.mc")
	public ModelAndView shopaddimpl(ModelAndView mv, ShopVO shop) {
		//��ü ���� ����Ʈ
		ArrayList<ShopVO> list = null;

		//���� �̹��� ���
		String imgname = shop.getMf().getOriginalFilename();
		shop.setImg1(imgname);
		shop.setImg2(imgname);
		shop.setImg3(imgname);

		try {
			sbiz.register(shop);
			Util.saveFile(shop.getMf());
			list = sbiz.get();
		} catch (Exception e) {
			mv.addObject("centerpage", "shop/registerfail");
			e.printStackTrace();
		}

		mv.addObject("registershop", shop);
		mv.addObject("shoplist", list);
		mv.addObject("centerpage", "shop/shop_list");
		mv.setViewName("main");

		return mv;
	}

	//���� ����Ʈ ȭ��
	@RequestMapping("/shop_list.mc")
	public ModelAndView shopselect(ModelAndView mv) {
		ArrayList<ShopVO> list = null;
		try {
			list = sbiz.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.addObject("shoplist", list);
		mv.addObject("centerpage", "shop/shop_list");
		mv.setViewName("main");
		return mv;
	}

	//���� �� ������
	@RequestMapping("/shop_detail.mc")
	public ModelAndView shopdetail(ModelAndView mv, Integer shopid) {

		//�Խ��� ���� & ������� & ��õ ��
		ShopVO dbshop = null;
		Shop_recommendVO shoprecommend = null;
		ArrayList<Shop_commentVO> shop_comment = null;

		try {
			dbshop = sbiz.get(shopid);
			shop_comment = fcomment.comment(shopid);
			shoprecommend = rbiz.get(shopid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//�Խ��� ���� & ������� �Ѱ��ֱ�
		mv.addObject("shopdetail", dbshop);
		mv.addObject("shop_comment", shop_comment);
		mv.addObject("shoprecommend", shoprecommend);
		mv.addObject("centerpage", "shop/shop_detail");
		mv.setViewName("main");
		return mv;
	}

	//���� ��� ��� �Ϸ�
	@RequestMapping("/shop_commentimpl.mc")
	public String shop_commentimpl(Shop_commentVO shop_comment) {

		//���� �̹��� ���
		String imgname = shop_comment.getMf().getOriginalFilename();
		shop_comment.setCommentImg(imgname);

		try {
			cbiz.register(shop_comment);
			Util.saveCommentFile(shop_comment.getMf());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:shop_detail.mc?shopid="+shop_comment.getShopid();
	}

	//���� ��õ �Ϸ�
	@RequestMapping("/shop_recommendimpl.mc")
	public String shop_recommendimpl(HttpServletRequest request) {

		//ajax������� �޾ƿ� ������ ����
		int shopid = Integer.parseInt(request.getParameter("shopid"));
		String userid = request.getParameter("userid");
		boolean up = Boolean.valueOf(request.getParameter("up")).booleanValue();
		boolean down = Boolean.valueOf(request.getParameter("down")).booleanValue();
		//������ ������ ��ü�� ����
		Shop_recommendVO recommend = new Shop_recommendVO(shopid, userid, up, down);

		try {
			rbiz.register(recommend);//DB�� ����
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:shop_detail.mc?shopid="+shopid;

	}

	//���� ��ġ �˻�(���� & ���Ը���Ʈ �����ֱ�)
	@RequestMapping("/search.mc")
	public ModelAndView search(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) {

		//�˻����� & �������� �޾ƿ���
		String loc = request.getParameter("loc");
		ArrayList<ShopVO> list = null;
		
		try {
			list = sbiz.get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!loc.equals("")) {
			mv.addObject("address", loc);
			mv.addObject("shoplist", list);
			mv.addObject("centerpage", "shop/shop_list");	//�˻����� shop_list.jsp�� ����
		}else {
			System.out.println("�˻�����"); //�׽�Ʈ��
		}
		mv.setViewName("main");
		
		return mv;
	}

	//���� ��ġ �����浵(���� ����) �޾ƿ���
	@RequestMapping("/getshopdata.mc")
	@ResponseBody
	public void getshopdata(HttpServletResponse res) throws Exception {

		ArrayList<ShopVO> slist = new ArrayList<>();
		try {
			slist = sbiz.get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONArray ja = new JSONArray();

		for(ShopVO s:slist) {
			JSONObject data = new JSONObject();
			data.put("shopname", s.getShopname());
			data.put("lat", (Math.round(s.getLat()*1000000)/1000000.0));
			data.put("lon", (Math.round(s.getLon()*1000000)/1000000.0));
			data.put("shopphonenumber", s.getShopphonenumber());
			ja.add(data);
		}

		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		out.print(ja.toJSONString());
		out.close();

	}

	//���� ��õ ���� �޾ƿ���
	@RequestMapping("/getshopdata2.mc")
	@ResponseBody
	public void getshopdata2(HttpServletResponse res) throws Exception {

		ArrayList<Shop_recommendVO> rlist = new ArrayList<>();
		
		try {
			rlist = rbiz.get();	//shopid�� �޾ƿ���
		} catch (Exception e) {
			e.printStackTrace();
		}		

		JSONArray ja2 = new JSONArray();

		for(Shop_recommendVO r:rlist) {
			Shop_recommendVO shoprecommend = null;
			JSONObject data = new JSONObject();
			data.put("shopid", r.getShopid());
			try {
				shoprecommend = rbiz.get(r.getShopid());
			} catch (Exception e) {
				e.printStackTrace();
			}	
			
			System.out.println("��ü�׽�Ʈ : " + shoprecommend);
			//��õ�� ����, ����õ�� ���� json�����Ϳ� �ֱ�
			data.put("up", shoprecommend.getUpcount());
			data.put("down", shoprecommend.getDowncount());
			ja2.add(data);
		}

		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		out.print(ja2.toJSONString());
		out.close();

	}


}
