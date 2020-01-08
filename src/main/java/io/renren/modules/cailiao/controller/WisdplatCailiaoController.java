package io.renren.modules.cailiao.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.renren.common.exception.RRException;
import io.renren.common.utils.ExcelReadUtils;
import io.renren.common.utils.FileUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.utils.UploadFileUtils;
import io.renren.modules.cailiao.entity.WisdplatCailiaoEntity;
import io.renren.modules.cailiao.entity.WisdplatCailiaoTableEntity;
import io.renren.modules.cailiao.entity.WisdplatCurveEntity;
import io.renren.modules.cailiao.entity.WisdplatLCSREntity;
import io.renren.modules.cailiao.service.WisdplatCailiaoService;
import io.renren.modules.cailiao.service.WisdplatCailiaoTableService;
import io.renren.modules.cailiao.service.WisdplatCurveService;
import io.renren.modules.cailiao.service.WisdplatLCSRService;



/**
 * 材料信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-25 13:51:16
 */
@RestController
@RequestMapping("cailiao/wisdplatcailiao")
public class WisdplatCailiaoController {

	private Logger logger= LoggerFactory.getLogger(WisdplatCailiaoController.class);

    @Autowired
    private WisdplatCailiaoService wisdplatCailiaoService;

    @Autowired
    private WisdplatCurveService wisdplatCurveService;
    @Autowired
    private WisdplatCailiaoTableService wisdplatTableService;
    @Autowired
    private WisdplatLCSRService wisdplatLcsrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("cailiao:wisdplatcailiao:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wisdplatCailiaoService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{clId}")
    @RequiresPermissions("cailiao:wisdplatcailiao:info")
    public R info(@PathVariable("clId") Long clId){
    	WisdplatCailiaoEntity wcl=wisdplatCailiaoService.getById(clId);
    	List list=new ArrayList();
    	if(0!=clId && null!=clId) {
    		list=wisdplatCurveService.findCurveInfoById(clId);
    	}
    	wcl.setCurveIdList(list);
        return R.ok().put("wisdplatCailiao", wcl);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("cailiao:wisdplatcailiao:save")
    public R save(@RequestParam("clId") String clId,
    		@RequestParam("clNo") String clNo,
    		@RequestParam("clName") String clName,
    		@RequestParam("clPaihao") String clPaihao,
    		@RequestParam("clWendu") String clWendu,
    		@RequestParam("clChangjia") String clChangjia,
    		@RequestParam("clMidu") String clMidu,
    		@RequestParam("clBosongbi") String clBosongbi,
    		@RequestParam("clMoliang") String clMoliang,
    		@RequestParam("clSigy") String clSigy,
    		@RequestParam("clEtan") String clEtan,
    		@RequestParam("clFail") String clFail,
    		@RequestParam("clC") String clC,
    		@RequestParam("clP") String clP,
    		@RequestParam("clFenleiId") String clFenleiId,
    		@RequestParam("clFileAddr") String clFileAddr,
    		@RequestParam(value="file",required =false) MultipartFile files,HttpServletRequest request){

		String strClNo = clNo;

		QueryWrapper<WisdplatCailiaoEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("cl_no",strClNo);
		int count = wisdplatCailiaoService.count(queryWrapper);
		if (count > 0) {
			// fileClientUpLoadAndUpLoadService.deleteFileByFileId(fileId);
			throw new RRException("材料编号重复!", HttpStatus.SC_BAD_REQUEST);
		}else {
			//获取文件名
	    	String fileName="";
	    	if(null!=files && !"".equals(files)) {
	    		fileName=files.getOriginalFilename();
	    	}
	    	WisdplatCailiaoEntity wcl=new WisdplatCailiaoEntity();
	    	wcl.setClBosongbi(clBosongbi);
	    	wcl.setClChangjia(clChangjia);
	    	wcl.setClEtan(clEtan);
	    	wcl.setClFail(clFail);
	    	wcl.setClFenleiId(clFenleiId);
	    	wcl.setClFileAddr(clFileAddr);
	    	wcl.setClMidu(clMidu);
	    	wcl.setClC(clC);
	    	wcl.setClMoliang(clMoliang);
	    	wcl.setClName(clName);
	    	wcl.setClNo(clNo);
	    	wcl.setClP(clP);
	    	wcl.setClPaihao(clPaihao);
	    	wcl.setClSigy(clSigy);
	    	wcl.setClWendu(clWendu);
			wisdplatCailiaoService.save(wcl);
			
			//获取当前操作的材料ID，上传Excel然后读取Excel数据进行插入操作
			Long dbclId=wisdplatCailiaoService.maxId();
			if(null!=files && !"".equals(fileName) && null!=fileName) {
				String filePath=System.getProperty("user.dir")+"\\uploads\\";
				UploadFileUtils.uploadFile(filePath, files, request);
				//读取Excel文件，读取第一个sheet，第二个sheet,第三个sheet
				//读取LCSS表
				//读取第一组曲线-应变值
				//获取所有列然后循环取数据
				Workbook wb = ExcelReadUtils.readExcel(files); // 文件
				int sheetAt=0;
				Sheet sheet = wb.getSheetAt(sheetAt); // sheet
				int totalColumns=sheet.getRow(0).getPhysicalNumberOfCells();
				int totalRows=sheet.getPhysicalNumberOfRows();
				List<HashMap<String,String>> array=new ArrayList<HashMap<String, String>>();
				List listOneX=new ArrayList();
				List listOneY=new ArrayList();
				List listTwoX=new ArrayList();
				List listTwoY=new ArrayList();
				List listThreeX=new ArrayList();
				List listThreeY=new ArrayList();
				List listFourX=new ArrayList();
				List listFourY=new ArrayList();
				List listFiveX=new ArrayList();
				List listFiveY=new ArrayList();
				Workbook wbsStr =ExcelReadUtils.readExcel(files); // 文件
				Sheet sheetsStr= wbsStr.getSheetAt(0); // sheet
				Row rowStr=sheetsStr.getRow(0);
				String dbcurveIdInfoOne = (String)ExcelReadUtils.getCellFormatValue(rowStr.getCell(1));
//				HashSet<String> curveLcssOne=ExcelReadUtils.getColumnSet(files, 2, 1,0,0);
				if(null!=dbcurveIdInfoOne && !"".equals(dbcurveIdInfoOne)) {
					logger.info("曲线ID-1:"+dbcurveIdInfoOne);
					//遍历应力和应变
					List<String> StrLcssOneX= ExcelReadUtils.getColumnSet(files, 1, 3,0,0);//应变
					for(String objstrOneX:StrLcssOneX) {
						if(null!=objstrOneX && !"".equals(objstrOneX)) {
							listOneX.add(Double.parseDouble(objstrOneX));
						}
					}
					List<String> StrLcssOneY= ExcelReadUtils.getColumnSet(files, 2, 3,0,0);//应力
					for(String objstrOneY:StrLcssOneY) {
						if(null!=objstrOneY && !"".equals(objstrOneY)) {
							listOneY.add(objstrOneY);
						}
					}
				}
				//Collections.sort(listOneX);
				//logger.info("X-1:"+listOneX);
				//Collections.sort(listOneY);
				//logger.info("y-1:"+listOneY);
				if(null!=listOneX && listOneX.size()>0) {
					WisdplatCurveEntity wcuEntityOne=new WisdplatCurveEntity();
					for(int mm=0;mm<listOneX.size();mm++) {
						String oneX=String.valueOf(listOneX.get(mm));
						wcuEntityOne.setWcX(oneX);
						for(int mn=0;mn<=mm;mn++) {
							String oneY=String.valueOf(listOneY.get(mn));
							wcuEntityOne.setWcY(oneY);
						}
						wcuEntityOne.setWcXishu(String.valueOf(mm));
						wcuEntityOne.setWcNo(dbcurveIdInfoOne);
						wcuEntityOne.setWcCailiaoId(dbclId);
						wisdplatCurveService.insert(wcuEntityOne);
					}
				}
//				HashSet<String> curveLcssTwo=ExcelReadUtils.getColumnSet(files, 4, 1,0,0);
				Workbook wbsStrTwo =ExcelReadUtils.readExcel(files); // 文件
				Sheet sheetsStrTwo= wbsStrTwo.getSheetAt(0); // sheet
				Row rowStrTwo=sheetsStrTwo.getRow(0);
				String curveLcssTwo=(String)ExcelReadUtils.getCellFormatValue(rowStrTwo.getCell(3));
				if(null!=curveLcssTwo && !"".equals(curveLcssTwo)) {
					List<String> StrLcssTwoX= ExcelReadUtils.getColumnSet(files, 3, 3,0,0);//应变
					for(String objstrTwoX:StrLcssTwoX) {
						if(!"".equals(objstrTwoX) && null!=objstrTwoX) {
							listTwoX.add(Double.parseDouble(objstrTwoX));
						}
					}
					List<String> StrLcssTwoY= ExcelReadUtils.getColumnSet(files, 4, 3,0,0);//应力
					for(String objstrTwoY:StrLcssTwoY) {
						if(!"".equals(objstrTwoY) && null!=objstrTwoY) {
							listTwoY.add(objstrTwoY);
						}
					}
				}
				//Collections.sort(listTwoX);
				//Collections.sort(listTwoY);
				
				if(null!=listTwoX && listTwoX.size()>0) {
					WisdplatCurveEntity wcuEntitytwo=new WisdplatCurveEntity();
					for(int pp=0;pp<listTwoX.size();pp++) {
						String twoX=String.valueOf(listTwoX.get(pp));
						wcuEntitytwo.setWcX(twoX);
						for(int pn=0;pn<=pp;pn++) {
							String twoY=String.valueOf(listTwoY.get(pn));
							wcuEntitytwo.setWcY(twoY);
						}
						wcuEntitytwo.setWcXishu(String.valueOf(pp));
						wcuEntitytwo.setWcNo(curveLcssTwo);
						wcuEntitytwo.setWcCailiaoId(dbclId);
						wisdplatCurveService.insert(wcuEntitytwo);
					}
				}else {
					
				}
				
//				HashSet<String> curveLcssThree=ExcelReadUtils.getColumnSet(files, 6, 1,0,0);
				Workbook wbsStrThree =ExcelReadUtils.readExcel(files); // 文件
				Sheet sheetsStrThree= wbsStrThree.getSheetAt(0); // sheet
				Row rowStrThree=sheetsStrThree.getRow(0);
				String curveLcssThree=(String)ExcelReadUtils.getCellFormatValue(rowStrThree.getCell(5));
				if(null!=curveLcssThree && !"".equals(curveLcssThree)) {
//					dbcurveIdInfoThree=curveLcssThree.iterator().next();
					List<String> StrLcssThreeX= ExcelReadUtils.getColumnSet(files, 5, 3,0,0);//应变
					for(String objstrThreeX:StrLcssThreeX) {
						if(!"".equals(objstrThreeX) && null!=objstrThreeX) {
							listThreeX.add(Double.parseDouble(objstrThreeX));
						}
					}
					List<String> StrLcssThreeY= ExcelReadUtils.getColumnSet(files, 6,3,0,0);//应力
					for(String objstrThreeY:StrLcssThreeY) {
						if(!"".equals(objstrThreeY) && null!=objstrThreeY) {
							listThreeY.add(objstrThreeY);
						}
					}
				}
				//Collections.sort(listThreeX);
				//Collections.sort(listThreeY);
				
				if(null!=listThreeX && listThreeX.size()>0) {
					WisdplatCurveEntity wcuEntitythree=new WisdplatCurveEntity();
					for(int pi=0;pi<listThreeX.size();pi++) {
						String threeX=String.valueOf(listThreeX.get(pi));
						wcuEntitythree.setWcX(threeX);
						for(int pj=0;pj<=pi;pj++) {
							String threeY=String.valueOf(listThreeY.get(pj));
							wcuEntitythree.setWcY(threeY);
						}
						wcuEntitythree.setWcXishu(String.valueOf(pi));
						wcuEntitythree.setWcNo(curveLcssThree);
						wcuEntitythree.setWcCailiaoId(dbclId);
						wisdplatCurveService.insert(wcuEntitythree);
					}
				}
				
				
				
//				HashSet<String> curveLcssFour=ExcelReadUtils.getColumnSet(files, 8, 1,0,0);
				Workbook wbsStrFour=ExcelReadUtils.readExcel(files); // 文件
				Sheet sheetsStrFour= wbsStrFour.getSheetAt(0); // sheet
				Row rowStrFour=sheetsStrFour.getRow(0);
				String curveLcssFour=(String)ExcelReadUtils.getCellFormatValue(rowStrFour.getCell(7));
				if(null!=curveLcssFour && !"".equals(curveLcssFour)) {
//					dbcurveIdInfoFour=curveLcssFour.iterator().next();
					List<String> StrLcssFourX= ExcelReadUtils.getColumnSet(files, 7, 3,0,0);//应变
					for(String objstrFourX:StrLcssFourX) {
						if(!"".equals(objstrFourX) && null!=objstrFourX) {
							listFourX.add(Double.parseDouble(objstrFourX));
						}
					}
					List<String> StrLcssFourY= ExcelReadUtils.getColumnSet(files, 8,3,0,0);//应力
					for(String objstrFourY:StrLcssFourY) {
						if(!"".equals(objstrFourY) && null!=objstrFourY) {
							listFourY.add(objstrFourY);
						}
					}
				}
				//Collections.sort(listFourX);
				//Collections.sort(listFourY);
				if(null!=listFourX && listFourX.size()>0) {
					WisdplatCurveEntity wcuEntityfour=new WisdplatCurveEntity();
					for(int oi=0;oi<listFourX.size();oi++) {
						String fourX=String.valueOf(listFourX.get(oi));
						wcuEntityfour.setWcX(fourX);
						for(int oj=0;oj<=oi;oj++) {
							String fourY=String.valueOf(listFourY.get(oj));
							wcuEntityfour.setWcY(fourY);
						}
						wcuEntityfour.setWcXishu(String.valueOf(oi));
						wcuEntityfour.setWcNo(curveLcssFour);
						wcuEntityfour.setWcCailiaoId(dbclId);
						wisdplatCurveService.insert(wcuEntityfour);
					}
				}
				
				String dbcurveIdInfoFive="";
				Workbook wbs1 =ExcelReadUtils.readExcel(files); // 文件
				Sheet sheets1 = wbs1.getSheetAt(0); // sheet
				Row rowt=sheets1.getRow(0);
				dbcurveIdInfoFive= (String)ExcelReadUtils.getCellFormatValue(rowt.getCell(9));
				logger.info("曲线ID:"+dbcurveIdInfoFive);
				if(!"".equals(dbcurveIdInfoFive) && null!=dbcurveIdInfoFive) {
					List<String> StrLcssFiveX= ExcelReadUtils.getColumnSet(files, 9, 3,0,0);//应变
					for(String objstrFiveX:StrLcssFiveX) {
						if(!"".equals(objstrFiveX) && null!=objstrFiveX) {
							listFiveX.add(Double.parseDouble(objstrFiveX));
						}
					}
					List<String> StrLcssFiveY= ExcelReadUtils.getColumnSet(files, 10,3,0,0);//应力
					for(String objstrFiveY:StrLcssFiveY) {
						if(!"".equals(objstrFiveY) && null!=objstrFiveY) {
							listFiveY.add(objstrFiveY);
						}
					}
				}
				//Collections.sort(listFiveX);
				//Collections.sort(listFiveY);
				if(null!=listFiveX && listFiveX.size()>0) {
					WisdplatCurveEntity wcuEntityfive=new WisdplatCurveEntity();
					for(int ti=0;ti<listFiveX.size();ti++) {
						String fiveX=String.valueOf(listFiveX.get(ti));
						wcuEntityfive.setWcX(fiveX);
						for(int tj=0;tj<=ti;tj++) {
							String fiveY=String.valueOf(listFiveY.get(tj));
							wcuEntityfive.setWcY(fiveY);
						}
						wcuEntityfive.setWcXishu(String.valueOf(ti));
						wcuEntityfive.setWcNo(dbcurveIdInfoFive);
						wcuEntityfive.setWcCailiaoId(dbclId);
						wisdplatCurveService.insert(wcuEntityfive);
					}
				}
				/*
				int newsRows=totalRows-2;
				for(int i=1;i<totalColumns;i++) {
					if(i%2==0) {
						//循环获取曲线ID
						HashSet<String> curveLcss=ExcelReadUtils.getColumnSet(files, i, 1,0,0);
						String dbcurveIdInfo=curveLcss.iterator().next();
						HashMap<String,String> hmp=new HashMap<String,String>();
//						logger.info("循环获取曲线ID:"+dbcurveIdInfo);
//						listHeader.add(dbcurveIdInfo);
						HashSet<String> StrLcss = ExcelReadUtils.getColumnSet(files, i, 3,0,0);//Y
						for(String objstr:StrLcss) {
							hmp.put(dbcurveIdInfo, objstr);
						}
						array.add(hmp);
					}else {
						HashSet<String> StrLcss = ExcelReadUtils.getColumnSet(files, i, 3,0,0);//X
//						for(String objstr:StrLcss) {
//							xInfo+=objstr+",";
//						}
						
					}
				}
				//遍历获取Y的值
				for (HashMap<String, String> hm : array) {
		            Set<String> set = hm.keySet();
		            for (String key : set) {
		                String value = hm.get(key);
		                logger.info("获取:"+key + "---" + value);
		            }
				}
				*/
				//读取LCSR表
				//获取曲线ID
				List<String> Strthree = ExcelReadUtils.getColumnSet(files, 2, 1,0,1);
				String quxianId=Strthree.iterator().next();
				List<String> Strfour=ExcelReadUtils.getColumnSet(files, 1, 3,0,1);
				List list=new ArrayList();
				String newsRate="";
				if(!"".equals(Strfour) && null!=Strfour) {
					for(String strOne:Strfour) {
						list.add(strOne);
					}
				}
				//Collections.sort(list);
				List listxs=new ArrayList();
				List<String> Strfive=ExcelReadUtils.getColumnSet(files, 2, 3,0,1);
				if(!"".equals(Strfive) && null!=Strfive) {
					for(String strTwo:Strfive) {
						listxs.add(strTwo);
					}
				}
				//Collections.sort(listxs);
				if(!"".equals(quxianId) && null!=quxianId) {
					WisdplatLCSREntity lcsrEntity=new WisdplatLCSREntity();
					lcsrEntity.setWcCailiaoId(dbclId);
					lcsrEntity.setLcsrNo(quxianId);
					if(!"".equals(list) && null!=list) {
						for(int k=0;k<list.size();k++) {
							String yingbianRate=String.valueOf(list.get(k));
							if(!"".equals(yingbianRate) && null!=yingbianRate) {
								lcsrEntity.setLcsrRate(yingbianRate);
								if(null!=listxs && listxs.size()>0) {
									for(int l=0;l<=k;l++) {
										String ydxishu=String.valueOf(listxs.get(l));
										lcsrEntity.setLcsrXishu(ydxishu);
									}
								}
								wisdplatLcsrService.insert(lcsrEntity);
							}
						}
					}
				}
				//读取TABLE表
				Workbook wbs =ExcelReadUtils.readExcel(files); // 文件
				Sheet sheets = wbs.getSheetAt(2); // sheet
				Row row=sheets.getRow(0);
				String dbtableId = (String)ExcelReadUtils.getCellFormatValue(row.getCell(1));
				List<String> StrTableTwo=ExcelReadUtils.getColumnSet(files, 1, 3,0,2);
				List listtbOne=new ArrayList();
				if(!"".equals(StrTableTwo) && null!=StrTableTwo) {
					for(String strTableRate:StrTableTwo) {
						if(!"".equals(strTableRate) && null!=strTableRate) {
							listtbOne.add(strTableRate);
						}
					}
				}
				//Collections.sort(listtbOne);
				List<String> StrTableThree=ExcelReadUtils.getColumnSet(files, 2, 3,0,2);
				List listtbTwo=new ArrayList();
				if(!"".equals(StrTableThree) && null!=StrTableThree) {
					for(String strTableCurve:StrTableThree) {
						if(!"".equals(strTableCurve) && null!=strTableCurve) {
							listtbTwo.add(strTableCurve);
						}
					}
				}
				//Collections.sort(listtbTwo);
				if(!"".equals(dbtableId) && null!=dbtableId) {
					WisdplatCailiaoTableEntity tableEntity=new WisdplatCailiaoTableEntity();
						tableEntity.setCtTableNo(dbtableId);
						tableEntity.setCtClId(dbclId);
						if(null!=listtbOne && listtbOne.size()>0) {
							for(int m=0;m<listtbOne.size();m++) {
								String dbybr=String.valueOf(listtbOne.get(m));
								tableEntity.setCtYingbianRate(dbybr);
								if(null!=listtbTwo && listtbTwo.size()>0) {
									for(int y=0;y<=m;y++) {
										String dbcurveId=String.valueOf(listtbTwo.get(y));
										tableEntity.setCtCurveId(dbcurveId);
									}
								}
								wisdplatTableService.insert(tableEntity);
							}
						}
				}
			}
	        return R.ok();
		}

    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cailiao:wisdplatcailiao:update")
    public R update(@RequestParam("clId") String clId,
    		@RequestParam("clNo") String clNo,
    		@RequestParam("clName") String clName,
    		@RequestParam("clPaihao") String clPaihao,
    		@RequestParam("clWendu") String clWendu,
    		@RequestParam("clChangjia") String clChangjia,
    		@RequestParam("clMidu") String clMidu,
    		@RequestParam("clBosongbi") String clBosongbi,
    		@RequestParam("clMoliang") String clMoliang,
    		@RequestParam("clSigy") String clSigy,
    		@RequestParam("clEtan") String clEtan,
    		@RequestParam("clFail") String clFail,
    		@RequestParam("clC") String clC,
    		@RequestParam("clP") String clP,
    		@RequestParam("clFenleiId") String clFenleiId,
    		@RequestParam("clFileAddr") String clFileAddr,
    		@RequestParam(value="file",required =false) MultipartFile files,HttpServletRequest request){
		//wisdplatCailiaoService.updateById(wisdplatCailiao);
        //获取材料ID，根据材料ID删除曲线相关信息，然后再新增
    	if(!"".equals(clId) && null!=clId) {
    		WisdplatLCSREntity lcsrEntity=new WisdplatLCSREntity();
    		lcsrEntity.setWcCailiaoId(Long.parseLong(clId));
    		wisdplatLcsrService.deleteInfo(lcsrEntity);
    		WisdplatCailiaoTableEntity wctEntity=new WisdplatCailiaoTableEntity();
    		wctEntity.setCtClId(Long.parseLong(clId));
    		wisdplatTableService.deleteInfo(wctEntity);
    		WisdplatCurveEntity  wcet=new WisdplatCurveEntity();
    		wcet.setWcCailiaoId(Long.parseLong(clId));
    		wisdplatCurveService.deleteInfo(wcet);
    		wisdplatCailiaoService.removeById(clId);
    	}
		//获取文件名
    	String fileName="";
    	if(null!=files && !"".equals(files)) {
    		fileName=files.getOriginalFilename();
    	}
    	WisdplatCailiaoEntity wcl=new WisdplatCailiaoEntity();
    	wcl.setClBosongbi(clBosongbi);
    	wcl.setClChangjia(clChangjia);
    	wcl.setClEtan(clEtan);
    	wcl.setClFail(clFail);
    	wcl.setClFenleiId(clFenleiId);
    	wcl.setClFileAddr(clFileAddr);
    	wcl.setClMidu(clMidu);
    	wcl.setClC(clC);
    	wcl.setClMoliang(clMoliang);
    	wcl.setClName(clName);
    	wcl.setClNo(clNo);
    	wcl.setClP(clP);
    	wcl.setClPaihao(clPaihao);
    	wcl.setClSigy(clSigy);
    	wcl.setClWendu(clWendu);
		wisdplatCailiaoService.save(wcl);
		
		//获取当前操作的材料ID，上传Excel然后读取Excel数据进行插入操作
		Long dbclId=wisdplatCailiaoService.maxId();
		if(null!=files && !"".equals(fileName) && null!=fileName) {
			String filePath=System.getProperty("user.dir")+"\\uploads\\";
			UploadFileUtils.uploadFile(filePath, files, request);
			//读取Excel文件，读取第一个sheet，第二个sheet,第三个sheet
			//读取LCSS表
			//读取第一组曲线-应变值
			//获取所有列然后循环取数据
			Workbook wb = ExcelReadUtils.readExcel(files); // 文件
			int sheetAt=0;
			Sheet sheet = wb.getSheetAt(sheetAt); // sheet
			int totalColumns=sheet.getRow(0).getPhysicalNumberOfCells();
			int totalRows=sheet.getPhysicalNumberOfRows();
			List<HashMap<String,String>> array=new ArrayList<HashMap<String, String>>();
			List listOneX=new ArrayList();
			List listOneY=new ArrayList();
			List listTwoX=new ArrayList();
			List listTwoY=new ArrayList();
			List listThreeX=new ArrayList();
			List listThreeY=new ArrayList();
			List listFourX=new ArrayList();
			List listFourY=new ArrayList();
			List listFiveX=new ArrayList();
			List listFiveY=new ArrayList();
			Workbook wbsStr =ExcelReadUtils.readExcel(files); // 文件
			Sheet sheetsStr= wbsStr.getSheetAt(0); // sheet
			Row rowStr=sheetsStr.getRow(0);
			String dbcurveIdInfoOne = (String)ExcelReadUtils.getCellFormatValue(rowStr.getCell(1));
//				HashSet<String> curveLcssOne=ExcelReadUtils.getColumnSet(files, 2, 1,0,0);
			if(null!=dbcurveIdInfoOne && !"".equals(dbcurveIdInfoOne)) {
				logger.info("曲线ID-1:"+dbcurveIdInfoOne);
				//遍历应力和应变
				List<String> StrLcssOneX= ExcelReadUtils.getColumnSet(files, 1, 3,0,0);//应变
				for(String objstrOneX:StrLcssOneX) {
					if(null!=objstrOneX && !"".equals(objstrOneX)) {
						listOneX.add(Double.parseDouble(objstrOneX));
					}
				}
				List<String> StrLcssOneY= ExcelReadUtils.getColumnSet(files, 2, 3,0,0);//应力
				for(String objstrOneY:StrLcssOneY) {
					if(null!=objstrOneY && !"".equals(objstrOneY)) {
						listOneY.add(objstrOneY);
					}
				}
			}
			if(null!=listOneX && listOneX.size()>0) {
				WisdplatCurveEntity wcuEntityOne=new WisdplatCurveEntity();
				for(int mm=0;mm<listOneX.size();mm++) {
					String oneX=String.valueOf(listOneX.get(mm));
					wcuEntityOne.setWcX(oneX);
					for(int mn=0;mn<=mm;mn++) {
						String oneY=String.valueOf(listOneY.get(mn));
						wcuEntityOne.setWcY(oneY);
					}
					wcuEntityOne.setWcXishu(String.valueOf(mm));
					wcuEntityOne.setWcNo(dbcurveIdInfoOne);
					wcuEntityOne.setWcCailiaoId(dbclId);
					wisdplatCurveService.insert(wcuEntityOne);
				}
			}
			String dbcurveIdInfoTwo="";
			List<String> curveLcssTwo=ExcelReadUtils.getColumnSet(files, 4, 1,0,0);
			if(null!=curveLcssTwo && !"".equals(curveLcssTwo)) {
				dbcurveIdInfoTwo=curveLcssTwo.iterator().next();
				List<String> StrLcssTwoX= ExcelReadUtils.getColumnSet(files, 3, 3,0,0);//应变
				for(String objstrTwoX:StrLcssTwoX) {
					if(!"".equals(objstrTwoX) && null!=objstrTwoX) {
						listTwoX.add(Double.parseDouble(objstrTwoX));
					}
				}
				List<String> StrLcssTwoY= ExcelReadUtils.getColumnSet(files, 4, 3,0,0);//应力
				for(String objstrTwoY:StrLcssTwoY) {
					if(!"".equals(objstrTwoY) && null!=objstrTwoY) {
						listTwoY.add(objstrTwoY);
					}
				}
			}
			
			if(null!=listTwoX && listTwoX.size()>0) {
				WisdplatCurveEntity wcuEntitytwo=new WisdplatCurveEntity();
				for(int pp=0;pp<listTwoX.size();pp++) {
					String twoX=String.valueOf(listTwoX.get(pp));
					wcuEntitytwo.setWcX(twoX);
					for(int pn=0;pn<=pp;pn++) {
						String twoY=String.valueOf(listTwoY.get(pn));
						wcuEntitytwo.setWcY(twoY);
					}
					wcuEntitytwo.setWcXishu(String.valueOf(pp));
					wcuEntitytwo.setWcNo(dbcurveIdInfoTwo);
					wcuEntitytwo.setWcCailiaoId(dbclId);
					wisdplatCurveService.insert(wcuEntitytwo);
				}
			}
			
			String dbcurveIdInfoThree="";
			List<String> curveLcssThree=ExcelReadUtils.getColumnSet(files, 6, 1,0,0);
			if(null!=curveLcssThree && !"".equals(curveLcssThree)) {
				dbcurveIdInfoThree=curveLcssThree.iterator().next();
				List<String> StrLcssThreeX= ExcelReadUtils.getColumnSet(files, 5, 3,0,0);//应变
				for(String objstrThreeX:StrLcssThreeX) {
					if(!"".equals(objstrThreeX) && null!=objstrThreeX) {
						listThreeX.add(Double.parseDouble(objstrThreeX));
					}
				}
				List<String> StrLcssThreeY= ExcelReadUtils.getColumnSet(files, 6,3,0,0);//应力
				for(String objstrThreeY:StrLcssThreeY) {
					if(!"".equals(objstrThreeY) && null!=objstrThreeY) {
						listThreeY.add(objstrThreeY);
					}
				}
			}
			
			if(null!=listThreeX && listThreeX.size()>0) {
				WisdplatCurveEntity wcuEntitythree=new WisdplatCurveEntity();
				for(int pi=0;pi<listThreeX.size();pi++) {
					String threeX=String.valueOf(listThreeX.get(pi));
					wcuEntitythree.setWcX(threeX);
					for(int pj=0;pj<=pi;pj++) {
						String threeY=String.valueOf(listThreeY.get(pj));
						wcuEntitythree.setWcY(threeY);
					}
					wcuEntitythree.setWcXishu(String.valueOf(pi));
					wcuEntitythree.setWcNo(dbcurveIdInfoThree);
					wcuEntitythree.setWcCailiaoId(dbclId);
					wisdplatCurveService.insert(wcuEntitythree);
				}
			}
			
			
			
			String dbcurveIdInfoFour="";
			List<String> curveLcssFour=ExcelReadUtils.getColumnSet(files, 8, 1,0,0);
			if(null!=curveLcssFour && !"".equals(curveLcssFour)) {
				dbcurveIdInfoFour=curveLcssFour.iterator().next();
				List<String> StrLcssFourX= ExcelReadUtils.getColumnSet(files, 7, 3,0,0);//应变
				for(String objstrFourX:StrLcssFourX) {
					if(!"".equals(objstrFourX) && null!=objstrFourX) {
						listFourX.add(Double.parseDouble(objstrFourX));
					}
				}
				List<String> StrLcssFourY= ExcelReadUtils.getColumnSet(files, 8,3,0,0);//应力
				for(String objstrFourY:StrLcssFourY) {
					if(!"".equals(objstrFourY) && null!=objstrFourY) {
						listFourY.add(objstrFourY);
					}
				}
			}
			if(null!=listFourX && listFourX.size()>0) {
				WisdplatCurveEntity wcuEntityfour=new WisdplatCurveEntity();
				for(int oi=0;oi<listFourX.size();oi++) {
					String fourX=String.valueOf(listFourX.get(oi));
					wcuEntityfour.setWcX(fourX);
					for(int oj=0;oj<=oi;oj++) {
						String fourY=String.valueOf(listFourY.get(oj));
						wcuEntityfour.setWcY(fourY);
					}
					wcuEntityfour.setWcXishu(String.valueOf(oi));
					wcuEntityfour.setWcNo(dbcurveIdInfoFour);
					wcuEntityfour.setWcCailiaoId(dbclId);
					wisdplatCurveService.insert(wcuEntityfour);
				}
			}
			
			String dbcurveIdInfoFive="";
			Workbook wbs1 =ExcelReadUtils.readExcel(files); // 文件
			Sheet sheets1 = wbs1.getSheetAt(0); // sheet
			Row rowt=sheets1.getRow(0);
			dbcurveIdInfoFive= (String)ExcelReadUtils.getCellFormatValue(rowt.getCell(9));
			logger.info("曲线ID:"+dbcurveIdInfoFive);
			if(!"".equals(dbcurveIdInfoFive) && null!=dbcurveIdInfoFive) {
				List<String> StrLcssFiveX= ExcelReadUtils.getColumnSet(files, 9, 3,0,0);//应变
				for(String objstrFiveX:StrLcssFiveX) {
					if(!"".equals(objstrFiveX) && null!=objstrFiveX) {
						listFiveX.add(Double.parseDouble(objstrFiveX));
					}
				}
				List<String> StrLcssFiveY= ExcelReadUtils.getColumnSet(files, 10,3,0,0);//应力
				for(String objstrFiveY:StrLcssFiveY) {
					if(!"".equals(objstrFiveY) && null!=objstrFiveY) {
						listFiveY.add(objstrFiveY);
					}
				}
			}
			if(null!=listFiveX && listFiveX.size()>0) {
				WisdplatCurveEntity wcuEntityfive=new WisdplatCurveEntity();
				for(int ti=0;ti<listFiveX.size();ti++) {
					String fiveX=String.valueOf(listFiveX.get(ti));
					wcuEntityfive.setWcX(fiveX);
					for(int tj=0;tj<=ti;tj++) {
						String fiveY=String.valueOf(listFiveY.get(tj));
						wcuEntityfive.setWcY(fiveY);
					}
					wcuEntityfive.setWcXishu(String.valueOf(ti));
					wcuEntityfive.setWcNo(dbcurveIdInfoFive);
					wcuEntityfive.setWcCailiaoId(dbclId);
					wisdplatCurveService.insert(wcuEntityfive);
				}
			}
			//读取LCSR表
			//获取曲线ID
			List<String> Strthree = ExcelReadUtils.getColumnSet(files, 2, 1,0,1);
			String quxianId=Strthree.iterator().next();
			List<String> Strfour=ExcelReadUtils.getColumnSet(files, 1, 3,0,1);
			List list=new ArrayList();
			String newsRate="";
			if(!"".equals(Strfour) && null!=Strfour) {
				for(String strOne:Strfour) {
					list.add(strOne);
				}
			}
			List listxs=new ArrayList();
			List<String> Strfive=ExcelReadUtils.getColumnSet(files, 2, 3,0,1);
			if(!"".equals(Strfive) && null!=Strfive) {
				for(String strTwo:Strfive) {
					listxs.add(strTwo);
				}
			}
			if(!"".equals(quxianId) && null!=quxianId) {
				WisdplatLCSREntity lcsrEntity=new WisdplatLCSREntity();
				lcsrEntity.setWcCailiaoId(dbclId);
				lcsrEntity.setLcsrNo(quxianId);
				if(!"".equals(list) && null!=list) {
					for(int k=0;k<list.size();k++) {
						String yingbianRate=String.valueOf(list.get(k));
						lcsrEntity.setLcsrRate(yingbianRate);
						if(null!=listxs && listxs.size()>0) {
							for(int l=0;l<=k;l++) {
								String ydxishu=String.valueOf(listxs.get(l));
								lcsrEntity.setLcsrXishu(ydxishu);
							}
						}
						wisdplatLcsrService.insert(lcsrEntity);
					}
				}
			}
			//读取TABLE表
			Workbook wbs =ExcelReadUtils.readExcel(files); // 文件
			Sheet sheets = wbs.getSheetAt(2); // sheet
			Row row=sheets.getRow(0);
			String dbtableId = (String)ExcelReadUtils.getCellFormatValue(row.getCell(1));
			List<String> StrTableTwo=ExcelReadUtils.getColumnSet(files, 1, 3,0,2);
			List listtbOne=new ArrayList();
			if(!"".equals(StrTableTwo) && null!=StrTableTwo) {
				for(String strTableRate:StrTableTwo) {
					if(!"".equals(strTableRate) && null!=strTableRate) {
						listtbOne.add(strTableRate);
					}
				}
			}
			List<String> StrTableThree=ExcelReadUtils.getColumnSet(files, 2, 3,0,2);
			List listtbTwo=new ArrayList();
			if(!"".equals(StrTableThree) && null!=StrTableThree) {
				for(String strTableCurve:StrTableThree) {
					if(!"".equals(strTableCurve) && null!=strTableCurve) {
						listtbTwo.add(strTableCurve);
					}
				}
			}
			if(!"".equals(dbtableId) && null!=dbtableId) {
				WisdplatCailiaoTableEntity tableEntity=new WisdplatCailiaoTableEntity();
					tableEntity.setCtTableNo(dbtableId);
					tableEntity.setCtClId(dbclId);
					if(null!=listtbOne && listtbOne.size()>0) {
						for(int m=0;m<listtbOne.size();m++) {
							String dbybr=String.valueOf(listtbOne.get(m));
							tableEntity.setCtYingbianRate(dbybr);
							if(null!=listtbTwo && listtbTwo.size()>0) {
								for(int y=0;y<=m;y++) {
									String dbcurveId=String.valueOf(listtbTwo.get(y));
									tableEntity.setCtCurveId(dbcurveId);
								}
							}
							wisdplatTableService.insert(tableEntity);
						}
					}
			}
		}
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("cailiao:wisdplatcailiao:delete")
    public R delete(@RequestBody Long[] clIds){
    	List lst=Arrays.asList(clIds);
    	for(Object obj:lst) {
    		List listwcl=wisdplatCurveService.queryListParentId(Long.parseLong(obj.toString()));
    		//根据材料ID删除LCSR的数据
    		WisdplatLCSREntity lcsrEntity=new WisdplatLCSREntity();
    		lcsrEntity.setWcCailiaoId(Long.parseLong(obj.toString()));
    		wisdplatLcsrService.deleteInfo(lcsrEntity);
    		//删除曲线数据
    		WisdplatCailiaoTableEntity wctEntity=new WisdplatCailiaoTableEntity();
    		wctEntity.setCtClId(Long.parseLong(obj.toString()));
    		wisdplatTableService.deleteInfo(wctEntity);
    	}
		wisdplatCailiaoService.removeByIds(Arrays.asList(clIds));
        return R.ok();
    }
    /**
     *     导 出材料模板
     * @return
     */
    @RequestMapping("/exportcailiao/{clId}")
    //@RequiresPermissions("cailiao:wisdplatcailiao:exportcailiao")
    public R exportcailiao(@PathVariable("clId") String clId,@RequestBody WisdplatCailiaoEntity wisdplatCailiao,HttpServletRequest request) {
    	String fileType=wisdplatCailiao.getFileType();//文件类型
    	String jiami=wisdplatCailiao.getJiami();//是否加密
    	//String panfu=wisdplatCailiao.getPanfu();//盘符
    	String fileurl=wisdplatCailiao.getFileurl();//文件路径
    	String curveId="";//曲线ID
    	String lcsrId="";
    	String tableNo="";
    	WisdplatCailiaoEntity wcl=null;
    	List listone=new ArrayList();
    	List listTwo=new ArrayList();
    	List listCurve=new ArrayList();
    	List listcl=new ArrayList();
    	List listtb=new ArrayList();
    	String uploadDir=null;
    	try {
    		//uploadDir=ResourceUtils.getURL("classpath:").getPath()+"/static/";
    		uploadDir=System.getProperty("user.dir")+"\\uploads\\";
		} catch (Exception e) {
			e.printStackTrace();
		}
    	logger.info("路径:"+uploadDir);
    	String downloadPath="";
    	StringBuffer sb=new StringBuffer();
    	if(!"".equals(clId) && null!=clId) {
    		String[] clidStr=clId.split(",");
    		if(null!=clidStr && !"".equals(clidStr)) {
    			if("1".equals(fileType)) {
    				//上传文件到指定目录
	        		sb.append("*KEYWORD"+"\r\n");
    			}
    			if("3".equals(fileType)) {
    				sb.append("CEND"+"\r\n");
    			}
    			for(int t=0;t<clidStr.length;t++) {
    				Long clidInfo=Long.parseLong(clidStr[t]);
    				//根据材料ID查询数据然后写入到txt
    	    		wcl=wisdplatCailiaoService.getById(clidInfo);
    	    		String clName=wcl.getClName();//材料名称
    	    		String clNo=wcl.getClNo();//材料编号
    	    		String clPaiNo=wcl.getClPaihao();//材料牌号
    	    		String clcj=wcl.getClChangjia();//材料厂家
    	    		String clwendu=wcl.getClWendu();//温度
    	    		String clmidu=wcl.getClMidu();//材料密度
    	    		String clMpa=wcl.getClMoliang();//弹性模量
    	    		String clpsbi=wcl.getClBosongbi();//泊松比
    	    		String clSigy=wcl.getClSigy();//SIGY
    	    		String clEtan=wcl.getClEtan();//ETAN
    	    		String clFail=wcl.getClFail();//FAIL
    	    		String clC=wcl.getClC();//C
    	    		String clP=wcl.getClP();//P
    	    		if("1".equals(fileType)) {
	    	    		sb.append("*MAT_PIECEWISE_LINEAR_PLASTICITY_TITLE"+"\r\n");
		            	sb.append(clName+"_"+clPaiNo+"_"+clcj+"\r\n");
		            	sb.append(clNo+","+clmidu+","+clMpa+","+clpsbi+","+clSigy+","+clEtan+","+clFail+","+"\r\n");
    	    		}
    	    		
    	    		Map<String,Object> map=new HashMap<String,Object>();
    	    		map.put("ctClId", clidInfo);
    	    		List listwcTable=wisdplatTableService.findProperty(map);
    	    		//根据材料ID查询scrId信息
    	    		Map<String,Object> maplcsr=new HashMap<String,Object>();
    	    		maplcsr.put("wcCailiaoId",clidInfo);
    	    		List listscr=wisdplatLcsrService.findProperty(maplcsr);
    	    		String dblcsrId="";
    	    		if(null!=listscr && listscr.size()>0) {
    	    			for(int tt=0;tt<listscr.size();tt++) {
    	    				WisdplatLCSREntity lcsrEntity=(WisdplatLCSREntity)listscr.get(tt);
    	    				lcsrId=lcsrEntity.getLcsrNo();
    	    			}
    	    		}
    	    		Long dbclIdInfo;
    	    		if(null!=listwcTable && listwcTable.size()>0) {
    	    			for(int ll=0;ll<listwcTable.size();ll++) {
    	    				WisdplatCailiaoTableEntity wcte=(WisdplatCailiaoTableEntity)listwcTable.get(ll);
    	    				tableNo=wcte.getCtTableNo();
    	    				listtb.add(tableNo);
    	    				String dbTablewcId=wcte.getCtCurveId();//Table里面的曲线ID
    	    				String yingbianRate=wcte.getCtYingbianRate();//应变率
    	    				listone.add(yingbianRate);
    	    				listTwo.add(dbTablewcId);
    	    				dbclIdInfo=wcte.getCtClId();
    	    			}
    	            	if("1".equals(fileType)) {
    	            		sb.append(clC+","+clP+","+tableNo+","+lcsrId+"\r\n");
    	            		sb.append("0,0,0"+"\r\n");
    	            		sb.append("0,0,0"+"\r\n");
    	            		if(!"".equals(tableNo) && null!=tableNo) {
            					sb.append("*DEFINE_TABLE"+"\r\n");
            					sb.append(tableNo+"\r\n");
            				}
    	            		
    	            	}
    	    		}else {
    	    			if("1".equals(fileType)) {
    	    				Map<String,Object> mappt=new HashMap<String,Object>();
            				mappt.put("wcCailiaoId", clidInfo);
            				List listcurve=wisdplatCurveService.findProperty(mappt);
    	    				String dbwcNoStr="";
    						for(int j=0;j<listcurve.size();j++) {
								WisdplatCurveEntity wcety=(WisdplatCurveEntity)listcurve.get(j);
								dbwcNoStr=wcety.getWcNo();
    						}
    						if(!"".equals(lcsrId) && null!=lcsrId) {
    							sb.append(clC+","+clP+","+dbwcNoStr+","+lcsrId+"\r\n");
    						}else {
    							sb.append(clC+","+clP+","+dbwcNoStr+",\r\n");
    						}
	    	    			sb.append("0,0,0"+"\r\n");
	    	            	sb.append("0,0,0"+"\r\n");
    	    			}
    	    		}
    	    		if("1".equals(fileType) && null!=listwcTable && listwcTable.size()>0) {
    	    			//循环读取Table信息
    	            	if(null!=listone && listone.size()>0) {
    	            		for(int ii=0;ii<listone.size();ii++) {
    	            			String ybrate=String.valueOf(listone.get(ii));
    	            			String curveIdInfo="";
    	            			if(null!=listTwo && listTwo.size()>0) {
    	            				for(int jj=0;jj<=ii;jj++) {
    	            					curveIdInfo=String.valueOf(listTwo.get(jj));
    	            				}
    	            			}
    	            			sb.append(ybrate+","+curveIdInfo+"\r\n");
    	            		}
    	            		listone.clear();
    	            	}
    	    		}
    	        	//Dyna
    	        	if("1".equals(fileType)) {
    	        		downloadPath=uploadDir+fileurl+".k";
    	            	//循环读取曲线信息,根据材料ID读取
    	            	Map<String,Object> mappt=new HashMap<String,Object>();
        				mappt.put("wcCailiaoId", clidInfo);
        				List listcurve=wisdplatCurveService.findProperty(mappt);
        				if(null!=listcurve && listcurve.size()>0) {
        					sb.append("*DEFINE_CURVE"+"\r\n");
        					if(null!=listTwo && listTwo.size()>0) {
	        					for(int m=0;m<listTwo.size();m++) {
	    							String dbtableNo=String.valueOf(listTwo.get(m));
	    							sb.append(dbtableNo+","+"0,0,0,0,0,0"+"\r\n");
	    							for(int j=0;j<listcurve.size();j++) {
	    								WisdplatCurveEntity wcety=(WisdplatCurveEntity)listcurve.get(j);
	    								String wcNoStr=wcety.getWcNo();
	    								if(dbtableNo.equals(wcNoStr)) {
	    									sb.append(wcety.getWcX()+","+wcety.getWcY()+"\r\n");
	    								}
	    							}
	    							if((m+1)<listTwo.size()) {
	    								sb.append("*DEFINE_CURVE"+"\r\n");
	    							}
	    						}
	        					listTwo.clear();
        					}else {
        						String dbwcNoStr="";
        						for(int j=0;j<listcurve.size();j++) {
    								WisdplatCurveEntity wcety=(WisdplatCurveEntity)listcurve.get(j);
    								dbwcNoStr=wcety.getWcNo();
        						}
								sb.append(dbwcNoStr+","+"0,0,0,0,0,0"+"\r\n");
        						for(int j=0;j<listcurve.size();j++) {
    								WisdplatCurveEntity wcety=(WisdplatCurveEntity)listcurve.get(j);
    								sb.append(wcety.getWcX()+","+wcety.getWcY()+"\r\n");
    							}
        					}
        				}
    	        	}
    	        	//ABAQUS
                	if("2".equals(fileType)) {
                		downloadPath=uploadDir+fileurl+".inp";
                		sb.append("*MATERIAL, NAME="+clName+"_"+clPaiNo+"_"+clcj+"\r\n");
                		sb.append("*DENSITY"+"\r\n");
                		sb.append(clmidu+","+"0.0"+"\r\n");
                		sb.append("*ELASTIC,TYPE=ISOTROPIC"+"\r\n");
                		sb.append(clMpa+","+clpsbi+","+"0.0"+"\r\n");
                		//查询曲线信息和dyna的X与Y相反
                		Map<String,Object> mappt=new HashMap<String,Object>();
                		if(!"".equals(clId) && null!=clId) {
        					mappt.put("wcCailiaoId", clidInfo);
        					List listcrvs=wisdplatCurveService.findProperty(mappt);
        					//查询材料Table表获取第一条曲线ID
        					Map<String,Object> mapct=new HashMap<String,Object>();
        					mapct.put("ctClId", clidInfo);
        					List listwts=wisdplatTableService.findProperty(mapct);
        					if(null!=listwts && listwts.size()>0) {
	        					String curIdStr=((WisdplatCailiaoTableEntity)listwts.get(0)).getCtCurveId();
	        					if(null!=listcrvs && listcrvs.size()>0) {
	        						sb.append("*PLASTIC"+"\r\n");
	        						for(int p=0;p<listcrvs.size();p++) {
	        							WisdplatCurveEntity  wcty=(WisdplatCurveEntity)listcrvs.get(p);
	        							if(curIdStr.equals(wcty.getWcNo())) {
	        								sb.append(wcty.getWcY()+","+wcty.getWcX()+","+"0.0"+"\r\n");
	        							}
	        						}
	        						
	        					}
        					}else {
        						if(null!=listcrvs && listcrvs.size()>0) {
	        						sb.append("*PLASTIC"+"\r\n");
	        						for(int p=0;p<listcrvs.size();p++) {
	        							WisdplatCurveEntity  wcty=(WisdplatCurveEntity)listcrvs.get(p);
	        							sb.append(wcty.getWcY()+","+wcty.getWcX()+","+"0.0"+"\r\n");
	        						}
	        						
	        					}
        					}
                		}
                		FileUtils.writeTxt(downloadPath,sb.toString());
                	}
                	//Nastran
                	if("3".equals(fileType)) {
                		downloadPath=uploadDir+fileurl+".bdf";
                		sb.append("$HMNAME MAT                 "+clNo+"\""+clName+"\" \"MAT1\""+"\r\n");
                		sb.append("MAT1,"+clNo+","+clMpa+","+clEtan+","+clpsbi+","+clmidu+""+"\r\n");
                		FileUtils.writeTxt(downloadPath,sb.toString());
                	}
                	if("1".equals(fileType)) {
                		//读取LCSR里面的记录，根据LCSR编号ID查询
        				if(!"".equals(lcsrId) && null!=lcsrId) {
        					sb.append("*DEFINE_CURVE"+"\r\n");
        					sb.append(""+lcsrId+",0,0,0,0,0,0"+"\r\n");
        					//查询LCSR信息
        					Map<String,Object> maplcsrs=new HashMap<String,Object>();
        					maplcsrs.put("wcCailiaoId",clidInfo);
        					maplcsrs.put("lcsrNo",lcsrId);
        					List listLcsr=wisdplatLcsrService.findProperty(maplcsrs);
        					if(null!=listLcsr && listLcsr.size()>0) {
        						for(int v=0;v<listLcsr.size();v++) {
        							WisdplatLCSREntity lcsrEntity=(WisdplatLCSREntity)listLcsr.get(v);
        							String lcsrRateStr=lcsrEntity.getLcsrRate();
        							String lcsrXiShu=lcsrEntity.getLcsrXishu();
        							sb.append(""+lcsrRateStr+","+lcsrXiShu+"\r\n");
        						}
        					}
        				}
                	}
                	
    			}
    			if("1".equals(fileType)) {
	    			sb.append("*END");
	            	FileUtils.writeTxt(uploadDir+fileurl+".k",sb.toString());
    			}
            	
            	
    		}
    	}
    	//根据材料ID查询TableId
    	//根据材料ID查询曲线ID
    	return R.ok();
    }
    /**
     * 	下载文件方法
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/download/{clId}")
    public String download(@PathVariable("clId") String clId,@RequestBody WisdplatCailiaoEntity wisdplatCailiao,HttpServletResponse response) throws UnsupportedEncodingException {
    	String fileType=wisdplatCailiao.getFileType();//文件类型
    	String jiami=wisdplatCailiao.getJiami();//是否加密
    	String fileurl=wisdplatCailiao.getFileurl();//文件路径
    	String uploadDir=null;
    	try {
    		uploadDir=System.getProperty("user.dir")+"\\uploads\\";
		} catch (Exception e) {
			e.printStackTrace();
		}
    	String downloadPath="";
    	String fileNameStr="";
    	if("1".equals(fileType)) {
			downloadPath=uploadDir+fileurl+".k";
			fileNameStr=fileurl+".k";
    	}
    	if("2".equals(fileType)) {
			downloadPath=uploadDir+fileurl+".inp";
			fileNameStr=fileurl+".inp";
    		
    	}
    	//Nastran
    	if("3".equals(fileType)) {
			downloadPath=uploadDir+fileurl+".bdf";
			fileNameStr=fileurl+".bdf";
    	}
    	File file=new File(downloadPath);
    	if(file.exists()) {
    		//设置强制下载不打开
		  response.setHeader("content-type", "application/octet-stream");
          response.setContentType("application/octet-stream");
          // 下载文件能正常显示中文
          response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileNameStr, "UTF-8"));
			byte[] buffer=new byte[1024];
			FileInputStream fis=null;
			BufferedInputStream bis=null;
			try {
				fis=new FileInputStream(file);
				bis=new BufferedInputStream(fis);
				OutputStream outputStream=response.getOutputStream();
				int k=bis.read(buffer);
				while(k!=-1) {
					outputStream.write(buffer,0,k);
					k=bis.read(buffer);
				}
				outputStream.flush();
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(bis!=null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(fis!=null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
    	}
    	return null;

    }

}
