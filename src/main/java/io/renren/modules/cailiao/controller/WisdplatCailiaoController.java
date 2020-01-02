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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		if(!"".equals(fileName) && null!=fileName) {
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
			//for(int i=1;i<totalColumns;i++) {
				HashSet<String> StrOneLcss = ExcelReadUtils.getColumnSet(files, 1, 3,0,0);//X
				HashSet<String> StrOneLcssY = ExcelReadUtils.getColumnSet(files, 2, 3,0,0);//Y
				HashSet<String> StrTwoLcss= ExcelReadUtils.getColumnSet(files, 2, 1,0,0);//读取指定列的指定行数数据
				String curveIdLcssOne=StrTwoLcss.iterator().next();
				
				String newsOneX="";
				String newsOneY="";
				if(!"".equals(StrOneLcss) && null!=StrOneLcss) {
					for(String sOneX:StrOneLcss) {
						newsOneX+=sOneX+",";
					}
				}
				if(!"".equals(StrOneLcssY) && null!=StrOneLcssY) {
					for(String sOne:StrOneLcssY) {
						newsOneY+=sOne+",";
					}
				}
				
				HashSet<String> StrThreeLcss = ExcelReadUtils.getColumnSet(files, 3, 3,0,0);
				String curveIdLcssTwo=StrThreeLcss.iterator().next();
				
				HashSet<String> StrFourLcss= ExcelReadUtils.getColumnSet(files, 4, 1,0,0);//读取指定列的指定行数数据
				String newsTwo="";
				String newsTwoX="";
				if(!"".equals(StrThreeLcss) && null!=StrThreeLcss) {
					for(String sTwoX:StrThreeLcss) {
						newsTwoX+=sTwoX+",";
					}
				}
				
				if(!"".equals(StrFourLcss) && null!=StrFourLcss) {
					for(String sTwo:StrFourLcss) {
						newsTwo+=sTwo+",";
					}
				}
				
				
				HashSet<String> StrFiveLcss = ExcelReadUtils.getColumnSet(files,5, 3,0,0);
				String curveIdLcssThree=StrFiveLcss.iterator().next();
				
				HashSet<String> StrSixLcss= ExcelReadUtils.getColumnSet(files, 6, 1,0,0);//读取指定列的指定行数数据
				String newsThree="";
				if(!"".equals(StrSixLcss) && null!=StrSixLcss) {
					for(String sThree:StrSixLcss) {
						newsThree+=sThree+",";
					}
				}
				
				
				HashSet<String> StrSevenLcss = ExcelReadUtils.getColumnSet(files,7,3,0,0);
				String curveIdLcssFour=StrSevenLcss.iterator().next();
				
				HashSet<String> StrEightLcss= ExcelReadUtils.getColumnSet(files, 8,1,0,0);//读取指定列的指定行数数据
				
				String newsFour="";
				if(!"".equals(StrEightLcss) && null!=StrEightLcss) {
					for(String sFour:StrEightLcss) {
						newsFour+=sFour+",";
					}
				}
				
				HashSet<String> StrNineLcss = ExcelReadUtils.getColumnSet(files,9,3,0,0);
				String curveIdLcssFive=StrNineLcss.iterator().next();
				
				HashSet<String> StTenLcss= ExcelReadUtils.getColumnSet(files,10,1,0,0);//读取指定列的指定行数数据
				String newsFive="";
				if(!"".equals(StTenLcss) && null!=StTenLcss) {
					for(String sFive:StTenLcss) {
						newsFive+=sFive+",";
					}
				}
				WisdplatCurveEntity wce=new WisdplatCurveEntity();
				if(!"".equals(newsOneX) && null!=newsOneX) {
					String[] nOneX=newsOneX.split(",");
					String[] nOneY=newsOneY.split(",");
					if(!"".equals(nOneX) && null!=nOneX) {
						for(int p=0;p<nOneX.length;p++) {
							//获取第一组
							wce.setWcNo(curveIdLcssOne);
							wce.setWcX(nOneX[p]);
							wce.setWcY(nOneY[p]);
							wce.setWcCailiaoId(dbclId);
							wce.setWcXishu(String.valueOf(p++));
							wisdplatCurveService.insert(wce);
						}
					}
				}
				/*
				if(!"".equals(newsTwo) && null!=newsTwo) {
					String[] nTwo=newsTwo.split(",");
					String[] nTwoX=newsTwoX.split(",");
					if(!"".equals(nTwo) && null!=nTwo) {
						for(int pj=0;pj<nTwo.length;pj++) {
							wce.setWcNo(curveIdLcssTwo);
							wce.setWcY(nTwo[pj]);
							wce.setWcX(nTwoX[pj]);
						}
					}
				}
				*/
				String[] nThree=newsThree.split(",");
				String[] nFour=newsFour.split(",");
				String[] nFive=newsFive.split(",");
				
				
				
			
			//}
			//读取LCSR表
			//获取曲线ID
			HashSet<String> Strthree = ExcelReadUtils.getColumnSet(files, 2, 1,0,1);
			String quxianId=Strthree.iterator().next();
			logger.info("曲线ID:"+quxianId);
			HashSet<String> Strfour=ExcelReadUtils.getColumnSet(files, 1, 3,0,1);
			logger.info("应变率"+Strfour.toString());
			String newsRate="";
			if(!"".equals(Strfour) && null!=Strfour) {
				for(String strOne:Strfour) {
					newsRate+=strOne+",";
				}
			}
			HashSet<String> Strfive=ExcelReadUtils.getColumnSet(files, 2, 3,0,1);
			logger.info("屈服强度缩放系数"+Strfive.toString());
			String newsXishu="";
			if(!"".equals(Strfive) && null!=Strfive) {
				for(String strTwo:Strfive) {
					newsXishu+=strTwo+",";
				}
			}
			String[] xishuStr=null;
			if(!"".equals(newsXishu) && null!=newsXishu) {
				xishuStr=newsXishu.split(",");
			}
			if(!"".equals(quxianId) && null!=quxianId) {
				WisdplatLCSREntity lcsrEntity=new WisdplatLCSREntity();
				lcsrEntity.setWcCailiaoId(dbclId);
				lcsrEntity.setLcsrNo(quxianId);
				if(!"".equals(newsRate) && null!=newsRate) {
					String[] nrStr=newsRate.split(",");
					if(null!=nrStr && !"".equals(nrStr)) {
						for(int q=0;q<nrStr.length;q++) {
							String yingbianRate=nrStr[q];
							String ydxishu=xishuStr[q];
							lcsrEntity.setLcsrRate(yingbianRate);
							lcsrEntity.setLcsrXishu(ydxishu);
							wisdplatLcsrService.insert(lcsrEntity);
						}
					}
				}
			}
			//读取TABLE表
			
			HashSet<String> StrTableOne = ExcelReadUtils.getColumnSet(files, 2, 1,0,2);
			String tableId=StrTableOne.iterator().next();
			logger.info("TableID:"+tableId);
			HashSet<String> StrTableTwo=ExcelReadUtils.getColumnSet(files, 1, 3,0,2);
			logger.info("Table表中应变率"+StrTableTwo.toString());
			String newsTableRate="";
			if(!"".equals(StrTableTwo) && null!=StrTableTwo) {
				for(String strTableRate:StrTableTwo) {
					newsTableRate+=strTableRate+",";
				}
			}
			
			HashSet<String> StrTableThree=ExcelReadUtils.getColumnSet(files, 2, 3,0,2);
			logger.info("Table表中曲线ID"+StrTableThree.toString());
			String newsTableCurve="";
			if(!"".equals(StrTableThree) && null!=StrTableThree) {
				for(String strTableCurve:StrTableThree) {
					newsTableCurve+=strTableCurve+",";
				}
			}
			
			if(!"".equals(tableId) && null!=tableId) {
				WisdplatCailiaoTableEntity tableEntity=new WisdplatCailiaoTableEntity();
				if(!"".equals(newsTableRate) && null!=newsTableRate) {
					String[] ntr=newsTableRate.split(",");
					String[] ntrY=newsTableCurve.split(",");
					if(!"".equals(ntr) && null!=ntr) {
						for(int m=0;m<ntr.length;m++) {
							tableEntity.setCtTableNo(tableId);
							tableEntity.setCtClId(dbclId);
							tableEntity.setCtYingbianRate(ntr[m]);
							tableEntity.setCtCurveId(ntrY[m]);
							wisdplatTableService.insert(tableEntity);
						}
					}
				}
			}
			
		}
		//logger.info("文件名:"+fileName+"当前插入ID:"+dbclId+"材料编号:"+clNo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cailiao:wisdplatcailiao:update")
    public R update(WisdplatCailiaoEntity wisdplatCailiao){
		wisdplatCailiaoService.updateById(wisdplatCailiao);
		
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
    public R exportcailiao(@PathVariable("clId") Long clId,@RequestBody WisdplatCailiaoEntity wisdplatCailiao,HttpServletRequest request) {
    	String fileType=wisdplatCailiao.getFileType();//文件类型
    	String jiami=wisdplatCailiao.getJiami();//是否加密
    	//String panfu=wisdplatCailiao.getPanfu();//盘符
    	String fileurl=wisdplatCailiao.getFileurl();//文件路径
    	String tableNo="";//table编号
    	String curveId="";//曲线ID
    	String lcsrId="";
    	WisdplatCailiaoEntity wcl=null;
    	List listone=new ArrayList();
    	List listTwo=new ArrayList();
    	List listCurve=new ArrayList();
    	if(!"".equals(clId) && null!=clId) {
    		//根据材料ID查询数据然后写入到txt
    		wcl=wisdplatCailiaoService.getById(clId);
    		Map<String,Object> map=new HashMap<String,Object>();
    		map.put("ctClId", clId);
    		List listwcTable=wisdplatTableService.findProperty(map);
    		if(null!=listwcTable && listwcTable.size()>0) {
    			Iterator itewct=listwcTable.iterator();
    			while(itewct.hasNext()) {
    				WisdplatCailiaoTableEntity wcte=(WisdplatCailiaoTableEntity)itewct.next();
    				tableNo=wcte.getCtTableNo();
    				String dbTablewcId=wcte.getCtCurveId();//Table里面的曲线ID
    				String yingbianRate=wcte.getCtYingbianRate();//应变率
    				listone.add(yingbianRate);
    			}
    		}
    		//根据材料ID查询曲线编号
    		Map<String,Object> mapt=new HashMap<String,Object>();
    		mapt.put("wcCailiaoId",clId);
    		List listwc=wisdplatCurveService.findProperty(mapt);
    		if(null!=listwc && listwc.size()>0) {
    			Iterator itewc=listwc.iterator();
    			while(itewc.hasNext()) {
    				WisdplatCurveEntity wce=(WisdplatCurveEntity)itewc.next();
    				curveId=wce.getWcNo();//曲线编号
    				Long wcId=wce.getWcId();//曲线主键ID
    				listTwo.add(wcId);
    				listCurve.add(curveId);//曲线编号
    			}
    		}
    		//根据材料ID查询LCSR信息
    		Map<String,Object> mapp=new HashMap<String,Object>();
    		mapp.put("wcCailiaoId",clId);
    		List listlcsr=wisdplatLcsrService.findProperty(mapp);
    		if(listlcsr!=null && listlcsr.size()>0) {
    			Iterator itelcsr=listlcsr.iterator();
    			while(itelcsr.hasNext()) {
    				WisdplatLCSREntity lcsr=(WisdplatLCSREntity)itelcsr.next();
    				lcsrId=lcsr.getLcsrNo();
    			}
    		}
    	}
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
    	//根据材料ID查询TableId
    	
    	//根据材料ID查询曲线ID
    	
    	String uploadDir=null;
    	try {
    		//uploadDir=ResourceUtils.getURL("classpath:").getPath()+"/static/";
    		uploadDir=System.getProperty("user.dir")+"\\uploads\\";
		} catch (Exception e) {
			e.printStackTrace();
		}
    	logger.info("路径:"+uploadDir);
    	StringBuffer sb=new StringBuffer();
    	//Dyna
    	String downloadPath="";
    	if("1".equals(fileType)) {
    		downloadPath=uploadDir+fileurl+".k";
    		//上传文件到指定目录
    		sb.append("*KEYWORD"+"\r\n");
        	sb.append("*MAT_PIECEWISE_LINEAR_PLASTICITY_TITLE"+"\r\n");
        	sb.append(clName+"_"+clPaiNo+"_"+clcj+"_"+clwendu+"\r\n");
        	sb.append(clNo+","+clmidu+","+clMpa+","+clpsbi+","+clSigy+","+clEtan+","+clFail+","+"\r\n");
        	sb.append(clC+","+clP+","+tableNo+","+lcsrId+"\r\n");
        	sb.append("0,0,0"+"\r\n");
        	sb.append("0,0,0"+"\r\n");
        	sb.append("*DEFINE_TABLE"+"\r\n");
        	//循环读取Table信息
        	sb.append(tableNo+"\r\n");
        	if(null!=listone && listone.size()>0) {
        		for(int i=0;i<listone.size();i++) {
        			String ybrate=String.valueOf(listone.get(i));
        			String curveIdInfo=String.valueOf(listCurve.get(i));
        			sb.append(ybrate+","+curveIdInfo+"\r\n");
        		}
        	}
        	//循环读取曲线信息
        	List listcurve=wisdplatCurveService.findProperty(null);
        	if(null!=listcurve && listcurve.size()>0) {
        		for(int j=0;j<listcurve.size();j++) {
        			WisdplatCurveEntity wcety=(WisdplatCurveEntity)listcurve.get(j);
        			sb.append("*DEFINE_CURVE"+"\r\n");
        			sb.append(wcety.getWcNo()+","+"0,0,0,0,0,0"+"\r\n");
        			sb.append(wcety.getWcX()+","+wcety.getWcY()+"\r\n");
        		}
        	}
        	sb.append("*END");
    		FileUtils.writeTxt(downloadPath,sb.toString());
    	}
    	//ABAQUS
    	if("2".equals(fileType)) {
    		downloadPath=uploadDir+fileurl+".inp";
    		sb.append("*MATERIAL, NAME="+clName+"\r\n");
    		sb.append("*DENSITY"+"\r\n");
    		sb.append(clmidu+","+"0.0"+"\r\n");
    		sb.append("*ELASTIC,TYPE=ISOTROPIC"+"\r\n");
    		sb.append(clMpa+","+clpsbi+","+"0.0"+"\r\n");
    		sb.append("*PLASTIC"+"\r\n");
    		//查询曲线信息和dyna的X与Y相反
    		List listcrvs=wisdplatCurveService.findProperty(null);
    		if(null!=listcrvs && listcrvs.size()>0) {
    			for(int p=0;p<listcrvs.size();p++) {
    				WisdplatCurveEntity  wcty=(WisdplatCurveEntity)listcrvs.get(p);
    				sb.append(wcty.getWcY()+","+wcty.getWcX()+","+"0.0"+"\r\n");
    			}
    		}
    		FileUtils.writeTxt(downloadPath,sb.toString());
    	}
    	//Nastran
    	if("3".equals(fileType)) {
    		downloadPath=uploadDir+fileurl+".bdf";
    		sb.append("CEND"+"\r\n");
    		sb.append("BEGIN BULK"+"\r\n");
    		sb.append("$HMNAME MAT                 "+clNo+"\""+clName+"\" \"MAT1\""+"\r\n");
    		sb.append("MAT1,"+clNo+","+clMpa+","+clEtan+","+clpsbi+","+clmidu+""+"\r\n");
    		sb.append("ENDDATA");
    		FileUtils.writeTxt(downloadPath,sb.toString());
    	}
    	return R.ok();
    }
    /**
     * 	下载文件方法
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping("/download")
    public String download(@RequestBody WisdplatCailiaoEntity wisdplatCailiao,HttpServletResponse response) throws UnsupportedEncodingException {
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