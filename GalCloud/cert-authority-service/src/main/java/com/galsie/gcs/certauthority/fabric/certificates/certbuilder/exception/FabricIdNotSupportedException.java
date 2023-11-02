package com.galsie.gcs.certauthority.fabric.certificates.certbuilder.exception;

public class FabricIdNotSupportedException extends Exception{

    public FabricIdNotSupportedException(long fabricId){
        super("The fabric id " + Long.toUnsignedString(fabricId) + " is not supported.");
    }
}
