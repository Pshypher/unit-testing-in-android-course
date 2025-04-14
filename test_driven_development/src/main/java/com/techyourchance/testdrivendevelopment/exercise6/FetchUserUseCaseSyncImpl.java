package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

public class FetchUserUseCaseSyncImpl implements FetchUserUseCaseSync {

    private final FetchUserHttpEndpointSync mFetchUserHttpEndpointSync;
    private final UsersCache mUserCache;

    public FetchUserUseCaseSyncImpl(FetchUserHttpEndpointSync endpoint, UsersCache cache) {
        mFetchUserHttpEndpointSync = endpoint;
        mUserCache = cache;
    }

    @Override
    public UseCaseResult fetchUserSync(String userId) {
        if (mUserCache.getUser(userId) != null) {
            return new UseCaseResult(Status.SUCCESS, mUserCache.getUser(userId));
        }

        try {
            FetchUserHttpEndpointSync.EndpointResult result = mFetchUserHttpEndpointSync.fetchUserSync(userId);
            if (isSuccessful(result)) {
                mUserCache.cacheUser(new User(result.getUserId(), result.getUsername()));
                return new UseCaseResult(Status.SUCCESS, mUserCache.getUser(userId));
            }
        } catch (NetworkErrorException e) {
            return new UseCaseResult(Status.NETWORK_ERROR, null);
        }

        return new UseCaseResult(Status.FAILURE, null);
    }

    private static boolean isSuccessful(FetchUserHttpEndpointSync.EndpointResult result) {
        return result.getStatus() == FetchUserHttpEndpointSync.EndpointStatus.SUCCESS;
    }
}
