/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxexercise;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author Administrator
 */
    public class MyDocument {
        private BooleanProperty isFlagged;
        private StringProperty fullpath;
        private StringProperty filename;
        private StringProperty keywords;
        private StringProperty content;
        
        public MyDocument(boolean isFlagged, String fullpath, String filename, String keywords, String content)
        {
            this.isFlagged = new SimpleBooleanProperty(isFlagged);
            this.fullpath = new SimpleStringProperty(fullpath);
            this.filename = new SimpleStringProperty(filename);
            this.keywords = new SimpleStringProperty(keywords);
            this.content = new SimpleStringProperty(content);
            
            this.isFlagged.addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    System.out.println(isFlaggedProperty().get() + " isFlagged: " + t1);
                }
            });            
        }

        public BooleanProperty isFlaggedProperty() { return isFlagged; }
        
        public StringProperty fullpathProperty() { return fullpath; }
 
        public StringProperty filenameProperty() { return filename; }

        public StringProperty keywordsProperty() { return keywords; }
 
        public StringProperty contentProperty() { return content; }

    }