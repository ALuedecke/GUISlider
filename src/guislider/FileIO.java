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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author ALuedecke
 */
public class FileIO {
    // Members

    public enum TemplateType {
        CUBE(0), ALBUM(1);
        
        private final int value;
        
        private TemplateType(int value) {
            this.value = value;
        }
        
        public int toInt() {
            return this.value;
        }
    }
    
    private static final Config CONFIG = new Config();

    private JTextArea output;

    public static Config getCONFIG() {
        return CONFIG;
    }

    public void setOutput(JTextArea output) {
        this.output = output;
    }

    // Constructor
    public FileIO(JTextArea output) {
        this.output = output;
    }
    
    // Public methods

    /**
     *
     * @param folder_name
     * @return List of file names
     */
    public List<String> getFolderContent(String folder_name) {
        File folder = new File(folder_name);
        FilenameFilter filter = (File path, String name) -> (
            name.toLowerCase().endsWith(".bmp") ||
            name.toLowerCase().endsWith(".gif") ||
            name.toLowerCase().endsWith(".jpg") ||
            name.toLowerCase().endsWith(".png")
        );
        List<String> names = new ArrayList<>();

        for (File file : folder.listFiles(filter)) {
            if (file.isFile()) {
                names.add(file.getName());
            }
        }

        return names;
    }

    /**
     *
     * @param file_names
     * @param template_idx
     * @param target_name
     */
    public void writeFile(List<String> file_names, TemplateType template_idx, String target_name) {
        BufferedWriter writer;
        File target;
        FileOutputStream out;
        String template;

        try {
            if (target_name.equals("")) {
                target = new File(CONFIG.getOutput_file()[template_idx.toInt()]);
            } else {
                target = new File(target_name);
            }
            out = new FileOutputStream(target);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            template = fillTemplate(file_names, template_idx);

            writer.write(template);
            writer.flush();
            writer.close();
            out.flush();
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
            output.setText(ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
            output.setText(ex.getMessage());
        }
    }

    /**
     *
     * @param file_names
     * @param template_idx
     */
    public void writeFile(List<String> file_names, TemplateType template_idx) {
        writeFile(file_names, template_idx, "");
    }

    /**
     *
     * @param img_folder
     * @param template_idx
     */
    public void writeFile(String img_folder, TemplateType template_idx) {
        writeFile(getFolderContent(img_folder), template_idx, "");
    }

    /**
     *
     * @param img_folder
     * @param template_idx
     * @param target_name
     */
    public void writeFile(String img_folder, TemplateType template_idx, String target_name) {
        output.setText("Image Folder: " + img_folder + "\n");
        writeFile(getFolderContent(img_folder), template_idx, target_name);
    }

    // Private methods

    private String fillAlbum(List<String> file_names) throws IOException {
        int col = 0;
        int idx = 0;
        int row = 0;
        
        StringBuilder script_tag = new StringBuilder();
        StringBuilder table_tag  = new StringBuilder();
        String script_line;
        String td_line;
        String template = getTemplate(TemplateType.ALBUM);

        output.setText(output.getText() +  "Selected Images:\n");
        
        for (String name : file_names) {
            script_line = CONFIG.getScript_tag();
            script_line = script_line.replaceAll(ReplaceTags.getPH_IMAGE_FILE(), name);
            script_line = script_line.replaceAll(ReplaceTags.getPH_ID(), "" + (idx));
            
            if (idx == 0) {
                script_tag.append(script_line);
            } else {
                script_tag.append("\n      ").append(script_line);
            }
            
            td_line = CONFIG.getImage_td_tag();
            td_line = td_line.replaceAll(ReplaceTags.getPH_IMAGE_FILE(), name);
            td_line = td_line.replaceAll(ReplaceTags.getPH_IMAGE_NAME(), name.substring(0, name.lastIndexOf(".")));
            td_line = td_line.replaceAll(ReplaceTags.getPH_ID(), "" + (idx));

            switch (col) {
                case 0:
                    table_tag.append("        <tr>\n").append("          ").append(td_line);
                    break;
                case 3:
                    table_tag.append("\n          ").append(td_line).append("\n        </tr>\n");
                    row++;
                    col = -1;
                    break;
                default:
                    table_tag.append("\n          ").append(td_line);
                    break;
            }

            output.setText(output.getText() +  " " + name + "\n");
            
            col++;
            idx++;
        }
        
        if (col > 0 && col <= 3) {
            for (int i = col; i <= 3 ; i++) {
                table_tag.append("\n          <td></td>");
            }
    
            table_tag.append("\n        </tr>");
        }

        template = template.replaceAll(ReplaceTags.getPH_SCRIPT(), script_tag.toString());
        template = template.replaceAll(ReplaceTags.getPH_TABLE_TAGS(), table_tag.toString());

        output.setText(output.getText() +  "\nFilled template:\n" + template);

        return template;
    }

    private String fillCube(List<String> file_names) throws IOException {
        int idx = 0;
        StringBuilder a_tag  = new StringBuilder();
        StringBuilder li_tag = new StringBuilder();
        String a_line;
        String li_line;
        String template = getTemplate(TemplateType.CUBE);

        output.setText(output.getText() +  "Selected Images:\n");
        
        for (String name : file_names) {
            a_line  = CONFIG.getBullet_tag();
            a_line = a_line.replaceAll(ReplaceTags.getPH_IMAGE_FILE(), name);
            a_line = a_line.replaceAll(ReplaceTags.getPH_IMAGE_NAME(), name.substring(0, name.lastIndexOf(".")));
            a_line = a_line.replaceAll(ReplaceTags.getPH_ID(), "" + (idx + 1));

            li_line = CONFIG.getImage_tag();
            li_line = li_line.replaceAll(ReplaceTags.getPH_IMAGE_FILE(), name);
            li_line = li_line.replaceAll(ReplaceTags.getPH_IMAGE_NAME(), name.substring(0, name.lastIndexOf(".")));
            li_line = li_line.replaceAll(ReplaceTags.getPH_ID(), "" + idx);

            if (idx == 0) {
                a_tag.append(a_line);
                li_tag.append(li_line);
            } else {
                a_tag.append("\n          ").append(a_line);
                li_tag.append("\n          ").append(li_line);
            }

            output.setText(output.getText() +  " " + name + "\n");

            idx++;
        }

        template = template.replaceAll(ReplaceTags.getPH_BULLET_TAGS(), a_tag.toString());
        template = template.replaceAll(ReplaceTags.getPH_IMAGE_TAGS(), li_tag.toString());

        output.setText(output.getText() +  "\nFilled template:\n" + template);

        return template;
    }
    
    private String fillTemplate(List<String> file_names, TemplateType template_idx) throws IOException {
        String template = "";
        
        switch (template_idx) {
            case ALBUM:
                template = fillAlbum(file_names);
                break;
            case CUBE:
                template = fillCube(file_names);
                break;
        }
        
        return template;
    }

    private String getTemplate(TemplateType template_idx) throws FileNotFoundException, IOException {
        boolean first_line = true;
        FileInputStream in = new FileInputStream(CONFIG.getTemplate_file()[template_idx.toInt()]);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            if (first_line) {
                out.append(line);
                first_line = false;
            } else {
                out.append("\n").append(line);
            }

        }

        return out.toString();
    }
}
