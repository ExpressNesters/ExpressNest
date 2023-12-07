import React, { useEffect, useState } from 'react';

const FollowComponent = () => {
    const [response, setResponse] = useState('');

    useEffect(() => {
        // Random IDs for demonstration
        const followerId = 1;
        const followeeId = 2;

        // Follow
        fetch('http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/follow', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ follower_id: followerId, followee_id: followeeId }),
        }).then(response => response.json())
          .then(data => setResponse(prev => prev + '\nFollow: ' + JSON.stringify(data)));

        // Unfollow
        fetch('http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/unfollow', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ follower_id: followerId, followee_id: followeeId }),
        }).then(response => response.json())
          .then(data => setResponse(prev => prev + '\nUnfollow: ' + JSON.stringify(data)));

        // Get Followers
        fetch(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/followers/${followeeId}`, {
            method: 'GET',
        }).then(response => response.json())
          .then(data => setResponse(prev => prev + '\nGet Followers: ' + JSON.stringify(data)));

        // Get Followees
        fetch(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/followees/${followerId}`, {
            method: 'GET',
        }).then(response => response.json())
          .then(data => setResponse(prev => prev + '\nGet Followees: ' + JSON.stringify(data)));

    }, []);

    return (
        <div>
            <h3>Server Responses:</h3>
            <pre>{response}</pre>
        </div>
    );
};

export default FollowComponent;

