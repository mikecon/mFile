
package gr.teicm.controller;

public interface ICommandStack {
    void execute();
    void undo();
    void redo();
}
