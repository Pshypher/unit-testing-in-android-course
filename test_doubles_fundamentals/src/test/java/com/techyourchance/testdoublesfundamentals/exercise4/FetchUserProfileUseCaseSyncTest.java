package com.techyourchance.testdoublesfundamentals.exercise4;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class FetchUserProfileUseCaseSyncTest {

    public static final String USER_ID = "userId";

    UserProfileHttpEndpointSyncTd mUserProfileHttpEndpointSync;
    UsersCacheTd mUsersCache;

    FetchUserProfileUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        mUserProfileHttpEndpointSync = new UserProfileHttpEndpointSyncTd();
        mUsersCache = new UsersCacheTd();
        SUT = new FetchUserProfileUseCaseSync(mUserProfileHttpEndpointSync, mUsersCache);
    }

    // fetch user profile calls UserProfileHttpEndpointSync.getUserProfile() with correct userId

    @Test
    public void fetchUserProfile_success_userIdPassedToEndpoint() {
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUserProfileHttpEndpointSync.mUserId, is(USER_ID));
    }

    // fetch user profile success caches returned user data
    @Test
    public void fetchUserProfile_success_cachesUserData() {
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUsersCache.getUser(USER_ID), is(instanceOf(User.class)));
    }

    // fetch user profile failed does not call UserCache.cacheUser()
    @Test
    public void fetchUserProfile_failureAuthError_cacheUserNotCalled() {
        mUserProfileHttpEndpointSync.mIsAuthError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUsersCache.mCounter, is(0));
    }

    @Test
    public void fetchUserProfile_failureServerError_cacheUserNotCalled() {
        mUserProfileHttpEndpointSync.mIsServerError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUsersCache.mCounter, is(0));
    }

    @Test
    public void fetchUserProfile_failureGeneralError_cacheUserNotCalled() {
        mUserProfileHttpEndpointSync.mIsGeneralError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUsersCache.mCounter, is(0));
    }


    // fetch user profile success returns success result
    @Test
    public void fetchUserProfile_success_successReturned() {
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS));
    }

    // fetch user profile failed returns failure result
    @Test
    public void fetchUserProfile_failedAuthError_failureReturned() {
        mUserProfileHttpEndpointSync.mIsAuthError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUserProfile_failedServerError_failureReturned() {
        mUserProfileHttpEndpointSync.mIsServerError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUserProfile_failedGeneralError_failureReturned() {
        mUserProfileHttpEndpointSync.mIsGeneralError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }
    // fetch user profile error returns network error result
    @Test
    public void fetchUserProfile_error_networkErrorReturned() {
        mUserProfileHttpEndpointSync.mIsNetworkError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }

    // --------------------------------------------------------------------------------------------
    private static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {

        public static final String FULL_NAME = "fullName";
        public static final String IMAGE_URL = "imageUrl";

        Boolean mIsAuthError = false;
        Boolean mIsServerError = false;
        Boolean mIsGeneralError = false;
        Boolean mIsNetworkError = false;

        String mUserId;


        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            mUserId = userId;

            EndpointResult result;
            if (mIsAuthError) {
                result = new EndpointResult(
                        EndpointResultStatus.AUTH_ERROR,
                        mUserId,
                        "",
                        ""
                );
            } else if (mIsServerError) {
                result = new EndpointResult(
                        EndpointResultStatus.SERVER_ERROR,
                        mUserId,
                        "",
                        ""
                );
            } else if (mIsGeneralError) {
                result = new EndpointResult(
                        EndpointResultStatus.GENERAL_ERROR,
                        mUserId,
                        "",
                        ""
                );
            } else if (mIsNetworkError) {
                throw new NetworkErrorException();
            } else {
                result = new EndpointResult(
                        EndpointResultStatus.SUCCESS,
                        mUserId,
                        FULL_NAME,
                        IMAGE_URL
                );
            }

            return result;
        }
    }

    private static class UsersCacheTd implements UsersCache {

        HashMap<String, User> mUsers;
        int mCounter;

        public UsersCacheTd() {
            mUsers = new HashMap<>();
        }

        @Override
        public void cacheUser(User user) {
            mCounter++;
            mUsers.put(user.getUserId(), user);
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            return mUsers.get(userId);
        }
    }
}