package com.techyourchance.testdrivendevelopment.exercise6;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class FetchUserUseCaseSyncTest {

    // region constants
    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    // endregion constants

    // region helper fields
    @Mock
    UsersCache mUserCacheMock;
    @Mock
    FetchUserHttpEndpointSync mFetchUserHttpEndpointSyncMock;
    // endregion helper fields

    FetchUserUseCaseSync SUT;

    @Before
    public void setup() throws Exception {
        SUT = new FetchUserUseCaseSyncImpl(mFetchUserHttpEndpointSyncMock, mUserCacheMock);
        success();
    }

    @Test
    public void fetchUserSync_userInCache_userIdNotPassedToEndpoint() throws Exception {
        inCache();
        SUT.fetchUserSync(USER_ID);
        verifyNoMoreInteractions(mFetchUserHttpEndpointSyncMock);
    }

    @Test
    public void fetchUserSync_userNotInCache_userIdPassedToEndpoint() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.fetchUserSync(USER_ID);
        verify(mFetchUserHttpEndpointSyncMock).fetchUserSync(ac.capture());
        assertThat(ac.getValue(), is(USER_ID));
    }

    @Test
    public void fetchUserSync_userIdNotInCache_successUserCached() throws Exception {
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.fetchUserSync(USER_ID);
        verify(mFetchUserHttpEndpointSyncMock).fetchUserSync(USER_ID);
        verify(mUserCacheMock).cacheUser(ac.capture());
        assertThat(ac.getValue(), is(instanceOf(User.class)));
    }

    @Test
    public void fetchUserSync_authError_userNotCached() throws Exception {
        authError();
        SUT.fetchUserSync(USER_ID);
        verify(mUserCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUserSync_generalError_userNotCached() throws Exception {
        generalError();
        SUT.fetchUserSync(USER_ID);
        verify(mUserCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUserSync_networkError_userNotCached() throws Exception {
        networkError();
        SUT.fetchUserSync(USER_ID);
        verify(mUserCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUserSync_userIdInCache_successReturnedWithCachedUser() throws Exception {
        inCache();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.SUCCESS));
        assertThat(result.getUser(), is(instanceOf(User.class)));
    }

    @Test
    public void fetchUserSync_userIdNotInCache_successReturnedWithUserFromPolledServer() throws Exception {
        runCache();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.SUCCESS));
        assertThat(result.getUser(), is(instanceOf(User.class)));
    }

    @Test
    public void fetchUserSync_authError_failureReturned() throws Exception {
        authError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.FAILURE));
        assertThat(result.getUser(), is(nullValue()));
    }

    @Test
    public void fetchUserSync_generalError_failureReturned() throws Exception {
        generalError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.FAILURE));
        assertThat(result.getUser(), is(nullValue()));
    }

    @Test
    public void fetchUserSync_networkError_networkExceptionThrown() throws Exception {
        networkError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.NETWORK_ERROR));
        assertThat(result.getUser(), is(nullValue()));
    }

    // region helper methods
    private void inCache() throws Exception {
        when(mUserCacheMock.getUser(USER_ID))
                .thenReturn(new User(USER_ID, USERNAME));
    }

    private void success() throws Exception {
        when(mFetchUserHttpEndpointSyncMock.fetchUserSync(USER_ID))
                .thenReturn(new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.SUCCESS, USER_ID, USERNAME));
    }

    private void runCache() throws Exception {
        success();
        inCache();
    }

    private void authError() throws Exception {
        when(mFetchUserHttpEndpointSyncMock.fetchUserSync(any(String.class)))
                .thenReturn(new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR, "", ""));
    }

    private void generalError() throws Exception {
        when(mFetchUserHttpEndpointSyncMock.fetchUserSync(any(String.class)))
                .thenReturn(new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.GENERAL_ERROR, "", ""));
    }

    private void networkError() throws Exception {
        doThrow(new NetworkErrorException())
                .when(mFetchUserHttpEndpointSyncMock).fetchUserSync(any(String.class));
    }
    // endregion helper methods
}