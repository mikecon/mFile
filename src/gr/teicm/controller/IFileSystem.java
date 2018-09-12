
package gr.teicm.controller;

import java.io.File;

public interface IFileSystem {
    File[] getRoots();
    File getHomeDirectory();
}
