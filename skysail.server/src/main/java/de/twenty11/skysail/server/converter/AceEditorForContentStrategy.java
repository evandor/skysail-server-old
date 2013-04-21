package de.twenty11.skysail.server.converter;

import java.util.List;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.descriptors.FileDescriptor;

public class AceEditorForContentStrategy extends AbstractHtmlCreatingStrategy {

    @Override
    public String createHtml(String page, Object skysailResponseAsObject, SkysailResponse<List<?>> skysailResponse) {

        if (skysailResponseAsObject instanceof FileDescriptor) {
            FileDescriptor filedescriptor = (FileDescriptor) skysailResponseAsObject;
            StringBuilder sb = new StringBuilder("<div id='editor'>");
            sb.append(filedescriptor.getContent());
            sb.append("</div>\n");
            // sb.append("<script src='http://rawgithub.com/ajaxorg/ace-builds/master/src-noconflict/ace.js' ");
            sb.append("<script src='/../static/js/ace-noconflict/ace.js' ");
            sb.append("type='text/javascript' charset='utf-8'></script>\n");
            sb.append("<script>\n");
            sb.append("  var editor = ace.edit('editor');\n");
            sb.append("  editor.setTheme('ace/theme/textmate');");
            if (filedescriptor.getExtention().equalsIgnoreCase(".java")) {
                sb.append("  editor.getSession().setMode('ace/mode/java');");
            } else {
                // sb.append("  editor.getSession().setMode('ace/mode/javascript');");
            }
            sb.append("</script>\n");

            page = page.replace("${content}", sb.toString());
        } else {
            page = page.replace("${content}", "could not create editor");
        }
        return page;
    }

}
