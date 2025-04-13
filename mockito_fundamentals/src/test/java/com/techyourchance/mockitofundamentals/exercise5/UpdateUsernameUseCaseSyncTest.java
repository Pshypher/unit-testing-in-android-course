package com.techyourchance.mockitofundamentals.exercise5;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

public class UpdateUsernameUseCaseSyncTest {

    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";

    UpdateUsernameHttpEndpointSync mUpdateUsernameHttpEndpointSyncMock;
    UsersCache mUsersCacheMock;
    EventBusPoster mEventBusPosterMock;

    UpdateUsernameUseCaseSync SUT;

    @Before
    public void setup() throws NetworkErrorException {
        mUpdateUsernameHttpEndpointSyncMock = mock(UpdateUsernameHttpEndpointSync.class);
        mUsersCacheMock = mock(UsersCache.class);
        mEventBusPosterMock = mock(EventBusPoster.class);
        SUT = new UpdateUsernameUseCaseSync(mUpdateUsernameHttpEndpointSyncMock, mUsersCacheMock, mEventBusPosterMock);

        success();
    }

    // update username with correct userId and username passed to endpoint
    @Test
    public void updateUsername_success_userIdAndUsernamePassedToEndpoint() throws NetworkErrorException {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(mUpdateUsernameHttpEndpointSyncMock).updateUsername(ac.capture(), ac.capture());
        List<String> args = ac.getAllValues();
        assertThat(args.get(0), is(USER_ID));
        assertThat(args.get(1), is(USERNAME));

    }

    // update username success updates the user cache with username
    @Test
    public void updateUsername_success_userCacheUpdated() {
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(mUsersCacheMock).cacheUser(ac.capture());
        User user = ac.getValue();
        assertThat(user.getUserId(), is(USER_ID));
        assertThat(user.getUsername(), is(USERNAME));
    }

    // update username failed does not interact with user cache
    @Test
    public void updateUsername_failureAuthError_userCacheNotUpdated() throws NetworkErrorException {
        authError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mUsersCacheMock);

    }

    @Test
    public void updateUsername_failureServerError_userCacheNotUpdated() throws NetworkErrorException {
        serverError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }

    @Test
    public void updateUsername_failureGeneralError_userCacheNotUpdated() throws NetworkErrorException {
        generalError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }

    @Test
    public void updateUsername_failureNetworkError_userCacheNotUpdated() throws NetworkErrorException {
        networkError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }

    // update username success posts event via event bus
    @Test
    public void updateUsername_success_eventBusPostCalled() {
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(mEventBusPosterMock).postEvent(ac.capture());
        assertThat(ac.getValue(), is(instanceOf(UserDetailsChangedEvent.class)));
    }

    // update username failed does not interact with event bus
    @Test
    public void updateUsername_failureAuthError_noPostEventOnEventBus() throws NetworkErrorException {
        authError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateUsername_failureServerError_noPostEventOnEventBus() throws NetworkErrorException {
        serverError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateUsername_failureGeneralError_noPostEventOnEventBus() throws NetworkErrorException {
        generalError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateUsername_failureNetworkError_noPostEventOnEventBus () throws NetworkErrorException {
        networkError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    // update username success returns success result
    @Test
    public void updateUsername_success_successReturned() {
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.SUCCESS));
    }

    // update username failed returns failure result
    @Test
    public void updateUsername_failureAuthError_failureReturned() throws NetworkErrorException {
        authError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsername_failureServerError_failureReturned() throws NetworkErrorException {
        serverError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsername_failureGeneralError_failureReturned() throws NetworkErrorException {
        generalError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsername_failureNetworkError_networkErrorReturned() throws NetworkErrorException {
        networkError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }
    // ---------------------------------------------------------------------------------------------

    private void success() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(USER_ID, USERNAME))
                .thenReturn(
                        new UpdateUsernameHttpEndpointSync.EndpointResult(
                                UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS,
                                USER_ID,
                                USERNAME
                        )
                );
    }

    private void authError() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(USER_ID, USERNAME))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(
                        UpdateUsernameHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                        "",
                        ""
                ));
    }

    private void serverError() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(USER_ID, USERNAME))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(
                        UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                        "",
                        ""
                ));
    }

    private void generalError() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(USER_ID, USERNAME))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(
                        UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                        "",
                        ""
                ));
    }

    private void networkError() throws NetworkErrorException {
        doThrow(new NetworkErrorException())
                .when(mUpdateUsernameHttpEndpointSyncMock)
                .updateUsername(USER_ID, USERNAME);
    }
}