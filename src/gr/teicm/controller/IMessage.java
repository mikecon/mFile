/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.teicm.controller;

/**
 *
 * @author Mike Con
 */
public interface IMessage {
    boolean showCopyFileMessage();
    boolean showOverwriteFileMessage();
    boolean showMessage(String title, String message);
}
