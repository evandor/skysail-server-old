package de.twenty11.skysail.server.presentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.twenty11.skysail.common.forms.Field;
import de.twenty11.skysail.common.responses.ConstraintViolationDetails;
import de.twenty11.skysail.common.responses.ConstraintViolationsResponse;
import de.twenty11.skysail.common.responses.FormResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;

public class FormForContentStrategy extends AbstractHtmlCreatingStrategy {

    @Override
    public String createHtml(String page, Object response, SkysailResponse<List<?>> skysailResponse) {
        Set<ConstraintViolationDetails> violations = null;
        Map<String, ConstraintViolationDetails> violationsMap = new HashMap<String, ConstraintViolationDetails>();

        String action = ".";
        if (skysailResponse instanceof ConstraintViolationsResponse) {
            ConstraintViolationsResponse cvr = (ConstraintViolationsResponse) skysailResponse;
            violations = cvr.getViolations();
            for (ConstraintViolationDetails violation : violations) {
                if (violation.getPropertyPath() != null) {
                    violationsMap.put(violation.getPropertyPath().toString(), violation);
                }
            }
            action = cvr.getActionReference().toString();
        } else if (skysailResponse instanceof FormResponse) {
            FormResponse formResponse = (FormResponse) skysailResponse;
            action = formResponse.getTarget();
        }

        StringBuilder sb = new StringBuilder("<form class='form-horizontal' action='" + action + "' method='POST'>\n");

        List<java.lang.reflect.Field> fields = getInheritedFields(response.getClass());// response.getClass().getFields();
        for (java.lang.reflect.Field field : fields) {
            Field formField = field.getAnnotation(Field.class);
            if (formField == null) {
                continue;
            }

            String value = "";
            try {
                // Field f = response.getClass().getDeclaredField("stuffIWant"); //NoSuchFieldException
                field.setAccessible(true);
                Object object = field.get(response);
                if (object instanceof String) {
                    value = (String) object;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String id = field.getName();
            String help = "";
            String cssClass = "control-group";
            if (violationsMap.containsKey(id)) {
                // Object object = constraintViolations.
                id = "inputError";
                cssClass = "control-group error";
                help = "<span class='help-inline'>" + violationsMap.get(field.getName()).getMessage() + "</span>";
            }

            sb.append("<div class='" + cssClass + "'>\n");
            sb.append("  <label class='control-label' for='" + id + "'>").append(field.getName()).append("</label>\n");
            sb.append("  <div class='controls'>\n");
            sb.append("<input type='text' id='" + id + "' name='" + field.getName() + "' placeholder='' value='")
                    .append(value).append("'>\n");
            sb.append(help);
            sb.append("  </div>\n");
            sb.append("</div>\n");

        }

        sb.append("<div class='control-group'>\n");
        sb.append("  <div class='controls'>\n");
        sb.append("    <button type='submit' class='btn'>Submit</button>\n");
        sb.append("  </div>\n");
        sb.append("</div>\n");

        sb.append("</form>\n");
        page = page.replace("${content}", sb.toString());
        return page;
    }

    private List<java.lang.reflect.Field> getInheritedFields(Class<?> type) {
        List<java.lang.reflect.Field> result = new ArrayList<java.lang.reflect.Field>();

        Class<?> i = type;
        while (i != null && i != Object.class) {
            while (i != null && i != Object.class) {
                for (java.lang.reflect.Field field : i.getDeclaredFields()) {
                    if (!field.isSynthetic()) {
                        result.add(field);
                    }
                }
                i = i.getSuperclass();
            }
        }

        return result;
    }

    private int handleDataElementsForTable(StringBuilder sb, int i, Object object) {
        StringBuilder row = new StringBuilder("<tr>\n");
        i++;

        sb.append("</tr>");
        sb.append(row).append("\n");
        return i;
    }

}
