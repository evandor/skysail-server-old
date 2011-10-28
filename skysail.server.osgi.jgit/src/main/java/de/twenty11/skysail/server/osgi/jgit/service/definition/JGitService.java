package de.twenty11.skysail.server.osgi.jgit.service.definition;

import java.io.IOException;

import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Repository;

public interface JGitService {

	Repository createRepository(String path) throws IOException;
	
    DirCache gitAdd(Repository rep, String filePattern) throws NoFilepatternException;

    void gitLog(Repository rep) throws NoHeadException, JGitInternalException;

}
