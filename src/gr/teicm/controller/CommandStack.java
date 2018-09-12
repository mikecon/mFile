
package gr.teicm.controller;

import java.util.LinkedList;

public class CommandStack {
    private LinkedList<ICommandStack> commandStack =
      new LinkedList<ICommandStack>();
   private LinkedList<ICommandStack> redoStack =
      new LinkedList<ICommandStack>();

   public void execute(ICommandStack command) {
      command.execute();
      commandStack.addFirst(command);
      redoStack.clear();
   }

   public void undo() {
      if (commandStack.isEmpty())
         return;
      ICommandStack command = commandStack.removeFirst();
      command.undo();
      redoStack.addFirst(command);
   }

   public void redo() {
      if (redoStack.isEmpty())
         return;
      ICommandStack command = redoStack.removeFirst();
      command.redo();
      commandStack.addFirst(command);
   }
}
