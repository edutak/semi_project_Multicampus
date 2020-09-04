package com.controller;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
		
		//�Խ��� ���� & �������
		ShopVO dbshop = null;
		Shop_recommendVO shoprecommend = null;
		ArrayList<Shop_commentVO> shop_comment = null;
		
		try {
			dbshop = sbiz.get(shopid);
			shop_comment = fcomment.comment(shopid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//�Խ��� ���� & ������� �Ѱ��ֱ�
		mv.addObject("shopdetail", dbshop);
		mv.addObject("shop_comment", shop_comment);
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
	


//    
//    @RequestMapping("/insert.mc")
//    public String insert(BoardVO board) {
//		try {
//			biz.register(board);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "redirect:main.mc";
//
//    }
//    
//	@RequestMapping("/read.mc")
//	public ModelAndView read(ModelAndView mv, Integer bid) {
//		BoardVO board = null;
//		try {
//			board = biz.get(bid);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		mv.addObject("data",board);
//		mv.setViewName("read");
//		return mv;
//	}
//	
//	@RequestMapping("/update.mc")
//	public ModelAndView update(ModelAndView mv, Integer bid) {
//		BoardVO board = null;
//		try {
//			board = biz.get(bid);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		mv.addObject("datas",board);
//		mv.setViewName("update");
//		return mv;
//	}
//	
//	@RequestMapping("/updateimpl.mc")
//	public String updateimpl(BoardVO board) {
//		try {
//			biz.modify(board);
//			System.out.println(board);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "redirect:main.mc";
//	}
//	
//	@RequestMapping("/delete.mc")
//    public String delete(Integer bid) {
//		try {
//			biz.remove(bid);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "redirect:main.mc";
//
//    }
	
	
	
	
//	@RequestMapping("/shopadd.mc")
//	public ModelAndView shopadd(ModelAndView mv) {
//		mv.addObject("centerpage", "shop/register");
//		mv.setViewName("main");
//		return mv;
//	}
//	
//	@RequestMapping("/shopdelete.mc")
//	public String shopdelete(Integer id) {
//		try {
//			biz.remove(id);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "redirect:shopselect.mc";
//	}
//	
//	@RequestMapping("/shopaddimpl.mc")
//	public ModelAndView shopaddimpl(ModelAndView mv, ShopVO shop) {
//		String imgname = shop.getMf().getOriginalFilename();
//		shop.setImg(imgname);
//		try {
//			biz.register(shop);
//			Util.saveFile(shop.getMf());
//		} catch (Exception e) {
//			mv.addObject("centerpage", "shop/registerfail");
//			e.printStackTrace();
//		}
//		mv.addObject("registershop", shop);
//		mv.addObject("centerpage", "shop/registerok");
//		mv.setViewName("main");
//		return mv;
//	}
//	
//	@RequestMapping("/shopselect.mc")
//	public ModelAndView shopselect(ModelAndView mv) {
//		ArrayList<ShopVO> list = null;
//		try {
//			list = biz.get();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		mv.addObject("shoplist", list);
//		mv.addObject("centerpage", "shop/get");
//		mv.setViewName("main");
//		return mv;
//	}
//	
//	@RequestMapping("/shopdetail.mc")
//	public ModelAndView shopdetail(ModelAndView mv, Integer id) {
//		
//		ShopVO dbshop = null;
//		
//		try {
//			dbshop = biz.get(id);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		mv.addObject("shopdetail", dbshop);
//		mv.addObject("centerpage", "shop/detail");
//		mv.setViewName("main");
//		return mv;
//	}
//	
//	@RequestMapping("/shopupdate.mc")
//	public ModelAndView shopupdate(ModelAndView mv, Integer id) {
//		
//		BoardVO dbshop = null;
//		
//		try {
//			dbshop = biz.get(id);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		mv.addObject("dbshop", dbshop);
//		mv.addObject("centerpage", "shop/modify");
//		mv.setViewName("main");
//		return mv;
//	}
//	
//	@RequestMapping("/shopupdateimpl.mc")
//	public String shopupdateimpl(BoardVO shop) {
//		String newimgname = shop.getMf().getOriginalFilename();
//		
//		if(! newimgname.contentEquals("")) {
//			shop.setImg(newimgname);
//			Util.saveFile(shop.getMf());
//		}
//		
//		try {
//			biz.modify(shop);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "redirect:shopdetail.mc?id="+shop.getId();
//	}
}
