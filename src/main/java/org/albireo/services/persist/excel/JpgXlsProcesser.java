/*
 * Copyright Cboard
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.albireo.services.persist.excel;

import org.apache.poi.ss.usermodel.*;

import java.util.Base64;

/**
 * Created by yfyuan on 2017/2/15.
 */
public class JpgXlsProcesser extends XlsProcesser {
    @Override
    protected ClientAnchor drawContent(XlsProcesserContext context) {
        String pngData = context.getData().getString("data");
        // data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABI4AAAEsCAYAAAClh/jbAAA ...
        String[] arr = pngData.split("base64,");
        byte[] bytes = Base64.getDecoder().decode(arr[1]);
        //byte[] bytes = Base64.getDecoder().decode(pngData.substring(23));
        int pictureIdx = context.getWb().addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
        Drawing drawing = context.getBoardSheet().createDrawingPatriarch();
        CreationHelper helper = context.getWb().getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();
        //HSSFClientAnchor anchor = new HSSFClientAnchor();
        anchor.setCol1(context.getC1());
        anchor.setRow1(context.getR1());
        anchor.setCol2(context.getC2());
        int r2 = context.getR2();
        if ("kpi".equals(context.getData().getString("widgetType"))) {
            r2 = context.getR1() + 8;
        }
        anchor.setRow2(r2);
        Picture picture = drawing.createPicture(anchor, pictureIdx);
        return picture.getClientAnchor();
    }
}
