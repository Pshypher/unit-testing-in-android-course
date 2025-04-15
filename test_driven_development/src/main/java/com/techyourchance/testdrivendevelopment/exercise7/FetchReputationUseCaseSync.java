package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

public class FetchReputationUseCaseSync {

    public enum UseCaseStatus {
        FAILURE, SUCCESS
    }

    public static class UseCaseResult {

        private final UseCaseStatus mUseCaseStatus;
        private final int mReputation;

        public UseCaseResult(UseCaseStatus useCaseStatus, int reputation) {
            mUseCaseStatus = useCaseStatus;
            mReputation = reputation;
        }

        public UseCaseStatus getStatus() {
            return mUseCaseStatus;
        }

        public int getReputation() {
            return mReputation;
        }
    }

    private final GetReputationHttpEndpointSync mGetReputationHttpEndpointSync;

    public FetchReputationUseCaseSync(GetReputationHttpEndpointSync getReputationHttpEndpointSync) {
        mGetReputationHttpEndpointSync = getReputationHttpEndpointSync;
    }

    public UseCaseResult getReputationSync() {
        GetReputationHttpEndpointSync.EndpointResult result = mGetReputationHttpEndpointSync.getReputationSync();

        if (isSuccess(result)) {
            return new UseCaseResult(UseCaseStatus.SUCCESS, result.getReputation());
        } else {
            return new UseCaseResult(UseCaseStatus.FAILURE, result.getReputation());
        }
    }

    private boolean isSuccess(GetReputationHttpEndpointSync.EndpointResult result) {
        return result.getStatus() == GetReputationHttpEndpointSync.EndpointStatus.SUCCESS;
    }
}
