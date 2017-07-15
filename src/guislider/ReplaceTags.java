/*
 * Copyright (C) 2016 ALuedecke
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package guislider;

/**
 *
 * @author ALuedecke
 */
public final class ReplaceTags {
    // Constant members
    private static final String BULLET_TAG = "<a href=\"#\" title=\":IMAGE_NAME:\">"
                                           + "<span><img src=\"data/tooltips/:FILE_NAME:\" alt=\":IMAGE_NAME:\"/>"
                                           + ":ID:</span></a>";
    private static final String IMAGE_TAG  = "<li><img src=\"data/images/:FILE_NAME:\" alt=\":IMAGE_NAME:\" "
                                           + "title=\":IMAGE_NAME:\" id=\"wows1_:ID:\"/></li>";
    private static final String IMAGE_TD_TAG = "<td><img src=\"data/images/:FILE_NAME:\" alt=\":IMAGE_NAME:\" "
                                             + "id=\":ID:\" onclick=\"handle_click(':ID:')\" /></td>";
    private static final String PH_BULLET_TAGS = ":A_BULLET_TAGS:";
    private static final String PH_ID = ":ID:";
    private static final String PH_IMAGE_FILE = ":FILE_NAME:";
    private static final String PH_IMAGE_NAME = ":IMAGE_NAME:";
    private static final String PH_IMAGE_TAGS = ":LI_IMAGE_TAGS:";
    private static final String PH_SCRIPT     = ":SCRIPT:";
    private static final String PH_TABLE_TAGS = ":TABLE_TAGS:";
    private static final String SCRIPT_TAG = "set_picture(:ID:, \"pictures/gallery_2015/:FILE_NAME:\");";
    
    // Variable members
    private static String output_file[]   = {"output/slider.html", "output/album.html"};
    private static String template_file[] = {"template/cube.tmpl", "template/album.tmpl"};

    // Getter-/Setters
    
    public static String getBULLET_TAG() {
        return BULLET_TAG;
    }

    public static String getIMAGE_TAG() {
        return IMAGE_TAG;
    }

    public static String getIMAGE_TD_TAG() {
        return IMAGE_TD_TAG;
    }

    public static String getPH_BULLET_TAGS() {
        return PH_BULLET_TAGS;
    }

    public static String getPH_ID() {
        return PH_ID;
    }

    public static String getPH_IMAGE_FILE() {
        return PH_IMAGE_FILE;
    }

    public static String getPH_IMAGE_NAME() {
        return PH_IMAGE_NAME;
    }

    public static String getPH_IMAGE_TAGS() {
        return PH_IMAGE_TAGS;
    }

    public static String getPH_TABLE_TAGS() {
        return PH_TABLE_TAGS;
    }

    public static String getPH_SCRIPT() {
        return PH_SCRIPT;
    }

    public static String[] getOutput_file() {
        return output_file;
    }

    public static String getSCRIPT_TAG() {
        return SCRIPT_TAG;
    }

    public static void setOutput_file(String[] slider_file) {
        ReplaceTags.output_file = slider_file;
    }

    public static String[] getTemplate_file() {
        return template_file;
    }

    public static void setTemplate_file(String[] template_file) {
        ReplaceTags.template_file = template_file;
    }
}
