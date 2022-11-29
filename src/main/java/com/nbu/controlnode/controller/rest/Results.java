package com.nbu.controlnode.controller.rest;

public final class Results {

    public sealed interface Result permits Success, Failure {
        boolean isSuccessful();
    }

    public sealed interface Success extends Result permits Added, Deleted {
        @Override
        default boolean isSuccessful() {
            return true;
        }
    }

    public sealed interface Failure extends Result permits NodeNotFound, ServiceError {
        @Override
        default boolean isSuccessful() {
            return false;
        }

        String getMessage();
    }

    public static final Failure NodeNotFound = new NodeNotFound();
    public static final Failure SERVICE_ERROR = new ServiceError();

    public static final Success Deleted = new Deleted();
    public static final Success Added = new Added();

    public static final class NodeNotFound implements Failure {
        @Override
        public String getMessage() {
            return "Node not found";
        }
    }

    private static final class ServiceError implements Failure {
        @Override
        public String getMessage() {
            return "Upstream error";
        }
    }

    private static final class Deleted implements Success {}

    private static final class Added implements Success {}

}