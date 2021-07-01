package com.yoga.inventory.barcode.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yoga.core.base.BaseController;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ChainMap;
import com.yoga.core.data.MapConverter;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.StringUtil;
import com.yoga.inventory.barcode.dto.BarcodeDto;
import com.yoga.inventory.barcode.dto.QueryDto;
import com.yoga.inventory.barcode.enums.BarcodeType;
import com.yoga.inventory.barcode.model.Barcode;
import com.yoga.inventory.barcode.service.BarcodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Controller
@Api(tags = "条码管理")
@RequestMapping("/admin/inventory/barcode")
public class BarcodeController extends BaseController {

    @Autowired
    private BarcodeService barcodeService = null;

    @ResponseBody
    @RequiresAuthentication
    @ApiOperation(value = "条码内容查询")
    @GetMapping("/query.json")
    public ApiResult query(@Valid @ModelAttribute QueryDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Barcode barcode = barcodeService.getBarcode(dto.getBarcode());
        Map<String, Object> result = new MapConverter<>(new MapConverter.Converter<Barcode>(){
            @Override
            public void convert(Barcode item, ChainMap<String, Object> map) {
                try {
                    map.set("valid", !item.getInvalid())
                            .set("detail", JSONObject.parse(item.getDetail()));
                } catch (JSONException ex) {
                    map.set("valid", !item.getInvalid())
                            .set("detail", item.getDetail());
                }
            }
        }).build(barcode);
        return new ApiResult<>(result);
    }

    private Configuration buildConfig(String type, String height, String moduleWidth, String placement, String fontSize) {
        DefaultConfiguration cfg = new DefaultConfiguration("barcode");
        DefaultConfiguration child = new DefaultConfiguration(type);
        cfg.addChild(child);
        DefaultConfiguration attr;
        attr = new DefaultConfiguration("height");
        attr.setValue(height);
        child.addChild(attr);
        attr = new DefaultConfiguration("module-width");
        attr.setValue(moduleWidth);
        child.addChild(attr);
        if (StringUtil.isNotBlank(placement)) {
            attr = new DefaultConfiguration("human-readable");
            DefaultConfiguration subAttr = new DefaultConfiguration("placement");
            subAttr.setValue(placement);
            attr.addChild(subAttr);
            if (StringUtil.isNotBlank(fontSize)) {
                subAttr = new DefaultConfiguration("font-size");
                subAttr.setValue(fontSize);
                attr.addChild(subAttr);
            }
            child.addChild(attr);
        }
        return cfg;
    }

    @ResponseBody
    @RequiresAuthentication
    @ApiOperation(value = "条码生成")
    @GetMapping("/barcode")
    public void getBarcode(HttpServletResponse response, @Valid @ModelAttribute BarcodeDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        BarcodeType type = dto.getType();
        if (type == null) throw new BusinessException("无效的编码类型，仅支持" + BarcodeType.getAllCode());
        try {
            Configuration cfg;
            if (StringUtil.hasBlank(dto.getHeight(), dto.getModuleWidth())) {
                DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
                InputStream inputStream = this.getClass().getResourceAsStream(dto.getType().getName());
                cfg = builder.build(inputStream);
            } else {
                cfg = buildConfig(dto.getType().getCode(), dto.getHeight(), dto.getModuleWidth(), dto.getPlacement(), dto.getFontSize());
            }
            BarcodeUtil barcodeUtil = BarcodeUtil.getInstance();
            BarcodeGenerator barcodeGenerator = barcodeUtil.createBarcodeGenerator(cfg);
            response.setContentType("image/png");
            OutputStream out = response.getOutputStream();
            BitmapCanvasProvider provider = new BitmapCanvasProvider(out, "image/x-png", 300, BufferedImage.TYPE_BYTE_GRAY, true, 0);
            barcodeGenerator.generateBarcode(provider, dto.getBarcode());
            provider.finish();
            out.flush();
            out.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
