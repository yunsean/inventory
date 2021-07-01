package com.yoga.inventory.barcode.service;

import com.alibaba.fastjson.JSONObject;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.inventory.barcode.mapper.BarcodeMapper;
import com.yoga.inventory.barcode.model.Barcode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service("inventoryBarcodeService")
public class BarcodeService extends BaseService {
    static final Logger log = Logger.getLogger("BarcodeService");

    @Value("${inventory.barcode.app-key:}")
    private String appkey;

    @Autowired
    private BarcodeMapper barcodeMapper;

    public Barcode add(String code, String detail) {
        if (MapperQuery.create(Barcode.class)
            .andEqualTo("barcode", code)
            .count(barcodeMapper) > 0) throw new BusinessException("已存在该条码！");
        Barcode barcode = new Barcode(code, detail, LocalDateTime.now());
        barcodeMapper.insert(barcode);
        return barcode;
    }

    public void delete(String code) {
        Barcode barcode = MapperQuery.create(Barcode.class)
                .andEqualTo("barcode", code)
                .queryFirst(barcodeMapper);
        if (barcode == null) throw new BusinessException("不存在该条码！");
        barcodeMapper.delete(barcode);
    }

    public Barcode getBarcode(String code) {
        Barcode barcode = MapperQuery.create(Barcode.class)
                .andEqualTo("barcode", code)
                .queryFirst(barcodeMapper);
        if (barcode != null && !barcode.getInvalid()) return barcode;
        JSONObject jsonObject = queryBarcode(code);
        if (jsonObject == null) throw new BusinessException("查询条码错误！");
        barcode = add(code, jsonObject.toJSONString());
        return barcode;
    }
;
    private JSONObject queryBarcode(String code) {
        if (StringUtil.isBlank(appkey)) {
            log.severe("The appkey is not config.");
            return null;
        }
        Map<String, Object> body = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;
            {
                put("code", code);
                put("appkey", appkey);
            }
        };
        try {
            String json = HttpInvoke.postExecute("https://way.jd.com/showapi/barcode", body);
            log.info(json);
            JSONObject jsonObject = JSONObject.parseObject(json);
            int rcode = jsonObject.getInteger("code");
            if (rcode != 10000) return null;
            JSONObject result = jsonObject.getJSONObject("result");
            if (result == null) return null;
            JSONObject showapi_res_body = result.getJSONObject("showapi_res_body");
            boolean flag = showapi_res_body.getBoolean("flag");
            if (!flag) return null;
            return showapi_res_body;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
