package de.twenty11.skysail.server.osgi.jgit.service.definition;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class JGitServiceImpl implements JGitService {

    @Override
    public Repository createRepository(String path) throws IOException {
//        FileRepositoryBuilder builder = new FileRepositoryBuilder();
//        Repository repository = builder.setGitDir(new File(path)).readEnvironment() // scan
//                                                                                    // environment
//                                                                                    // GIT_*
//                                                                                    // variables
//                        .findGitDir() // scan up the file system tree
//                        .build();
//        repository.create();
//        return repository;
        File directory = new File(path);
        InitCommand command = new InitCommand();
        command.setDirectory(directory);
       return command.call().getRepository();
    }

    @Override
    public DirCache gitAdd(Repository rep, String filePattern) throws NoFilepatternException {
        Git git = new Git(rep);
        AddCommand add = git.add();
        DirCache call = add.addFilepattern(filePattern).call();
        return call;

    }

    @Override
    public void gitLog(Repository rep) throws NoHeadException, JGitInternalException {
        Git git = new Git(rep);
        LogCommand log = git.log();
        Iterable<RevCommit> call = log.call();
        for (RevCommit revCommit : call) {
            String fullMessage = revCommit.getFullMessage();
            System.out.println(fullMessage);
        }

    }

    public void gitHead(Repository rep) throws IOException {
        String fullBranch = rep.getFullBranch();
        System.out.println(fullBranch);
        try {
            // The following could also have been done using
            // repo.resolve("HEAD")
            ObjectId id = rep.resolve(rep.getFullBranch());
            if (id != null)
                System.out.println("Branch " + rep.getBranch() + " points to " + id.name());
        } catch (IOException ioe) {
            // Failed to resolve.
        }
        try {
            ObjectId parent = rep.resolve("HEAD^");
            ObjectId tree = rep.resolve("HEAD^{tree}");
            if (parent != null && tree != null)
                System.out.println("HEAD has parent " + parent.name() + " and tree " + tree.name());
        } catch (RevisionSyntaxException rse) {
            // Syntax error in the revision expression, e.g. HEAD~foo. or
            // HEAD^{foo}
        } catch (IncorrectObjectTypeException rse) {
            // Type expression applied to incorrect type, e.g. using ^{tree} on
            // blob ID.
        }
        StoredConfig config = rep.getConfig();
        String name = config.getString("user", null, "name");
        String email = config.getString("user", null, "email");
        if (name == null || email == null) {
                System.out.println("User identity is unknown!");
        } else {
                System.out.println("User identity is " + name + " <" + email + ">");
        }
        String url = config.getString("remote", "origin", "url");
        if (url != null) {
                System.out.println("Origin comes from " + url);
        }
    }

}
