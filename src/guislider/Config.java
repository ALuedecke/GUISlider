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

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ALuedecke
 */
public final class Config {
    
    // Variable members
    private String config_file = "config.json";

    private String bullet_tag;
    private String image_tag;
    private String image_td_tag;
    private String script_tag;
    
    private String[] output_file;
    private String[] template_file;
    
    
    // Constructors
    
    public Config(String config_file) {
        this.config_file = config_file;
        setConfiguration();
    }
    
    public Config() {
        setConfiguration();
    }
    
    // Getters

    public String getConfig_file() {
        return config_file;
    }

    public String getBullet_tag() {
        return bullet_tag;
    }

    public String getImage_tag() {
        return image_tag;
    }

    public String getImage_td_tag() {
        return image_td_tag;
    }

    public String getScript_tag() {
        return script_tag;
    }

    public String[] getOutput_file() {
        return output_file;
    }

    public String[] getTemplate_file() {
        return template_file;
    }
    
    // Private methods
    
    private JSONObject loadConfigFile() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        
        Object obj = parser.parse(new FileReader(config_file));
        
        return (JSONObject) ((JSONArray) obj).get(0);
    }
    
    public void setConfiguration() {
        boolean get_from_rpl_tags = false;
        
        try {
            JSONObject config = loadConfigFile();
            
            this.bullet_tag    = (String) config.get("bullet_tag");
            this.image_tag     = (String) config.get("image_tag");
            this.image_td_tag  = (String) config.get("image_td_tag");
            this.script_tag    = (String) config.get("script_tag");
            this.output_file   = toStringArray((JSONArray) config.get("output_file"));
            this.template_file = toStringArray((JSONArray) config.get("template_file"));
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.INFO, "no Config file");
            get_from_rpl_tags = true;
        } catch (ParseException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            get_from_rpl_tags = true;
        }
        
        if (get_from_rpl_tags) {
            this.bullet_tag    = ReplaceTags.getBULLET_TAG();
            this.image_tag     = ReplaceTags.getIMAGE_TAG();
            this.image_td_tag  = ReplaceTags.getIMAGE_TD_TAG();
            this.script_tag    = ReplaceTags.getSCRIPT_TAG();
            this.output_file   = ReplaceTags.getOutput_file();
            this.template_file = ReplaceTags.getTemplate_file();
        }
    }
    
    private String[] toStringArray(JSONArray json_array) {
        List<String> lst = new ArrayList<>();
        
        json_array.stream().forEach((obj) -> {
            lst.add((String) obj);
        });
        
        return lst.toArray(new String[lst.size()]);
    }
}
