package com.classmateapp.mobile;

import java.util.Map;

public abstract class Document {
	
	/** All of the fields for this doc **/
	protected Map<String, Object> mFields;
	
	/** The document's meteor id **/
	protected String mDocId;
	
	/**
     * Constructor for Document object
     * @param docId Meteor's object ID for this Document
     * @param fields field/value map reference
     */
	public Document(String docId, Map<String, Object> fields) {
		this.mFields = fields;
		this.mDocId = docId;
	}

	/**
     * Gets Meteor object ID
     * @return object ID string
     */
    public String getId() {
        return mDocId;
    }
    
    /**
     * Gets Meteor doc's fields map
     * @return doc's fields map
     */
    public Map<String, Object> getFields() {
    	return mFields;
    }
	
}
