package de.twenty11.skysail.server.um.resources;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Get;

import de.twenty11.skysail.common.Presentation;
import de.twenty11.skysail.common.PresentationStyle;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SuccessResponse;
import de.twenty11.skysail.common.selfdescription.ResourceDetails;
import de.twenty11.skysail.server.core.restlet.ListServerResource2;

/**
 * Restlet Root Resource for dbViewer application.
 * 
 */
@Presentation(preferred = PresentationStyle.LIST2)
public class UserManagementRootResource extends ListServerResource2<ResourceDetails> {

    public UserManagementRootResource() {
        setName("usermanagement root resource");
        setDescription("The root resource of the usermanagement application");
    }

    // @Override
    // @Get("html|json|csv")
    // public SkysailResponse<List<Component>> getEntities() {
    // registerLinkedPage(new LinkedPage() {
    //
    // @Override
    // public String getLinkText() {
    // return "new Folder";
    // }
    //
    // @Override
    // public String getHref() {
    // return "notes/folder";
    // }
    //
    // @Override
    // public boolean applicable() {
    // return true;
    // }
    // });
    // registerLinkedPage(new LinkedPage() {
    //
    // @Override
    // public String getLinkText() {
    // return "new Note";
    // }
    //
    // @Override
    // public String getHref() {
    // return "notes/note";
    // }
    //
    // @Override
    // public boolean applicable() {
    // return true;
    // }
    // });
    // return getEntities("Folders and Notes");
    // }

    @Override
    @Get("html|json|csv")
    public SkysailResponse<List<ResourceDetails>> getEntities() {
        SkysailResponse<List<ResourceDetails>> entities = getEntities("Public API of Skysail JGit Extension");
        List<ResourceDetails> data = entities.getData();
        List<ResourceDetails> result = new ArrayList<ResourceDetails>();
        for (ResourceDetails rd : data) {
            result.add(new ResourceDetails(rd.getPath(), rd.getText(), rd.getFinder(), rd.getDesc()));
        }
        return new SuccessResponse<List<ResourceDetails>>(result);
    }

    @Override
    protected List<ResourceDetails> getData() {
        return allMethods();
    }

}
