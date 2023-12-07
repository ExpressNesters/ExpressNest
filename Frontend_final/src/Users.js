import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; 
import "./Users.css"

const Users = ({ userId }) => {
  const [users, setUsers] = useState([]);
  const [followees, setFollowees] = useState([]);
  const [followersCount, setFollowersCount] = useState({});

  const navigate = useNavigate(); // Initialize navigate

  const viewPosts = (viewUserId) => {
    navigate(`/myposts/${viewUserId}`);
  };
  useEffect(() => {
    // Fetch all users
    axios.get('http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/users')
      .then(response => {
        setUsers(response.data);
      })
      .catch(error => {
        console.error('Error fetching users:', error);
      });

    // Fetch followees of the current user
    axios.get(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/followees/${userId}`)
      .then(response => {
        setFollowees(response.data.followees || []);
      })
      .catch(error => {
        console.error('Error fetching followees:', error);
      });
  }, [userId]);

  // Fetch followers count for each user
  useEffect(() => {
    users.forEach(user => {
        
      axios.get(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/followers/${user.ID}`)
        .then(response => {
            const followerCount = response.data ? response.data.number_of_followers : 0;
            setFollowersCount(prev => ({ ...prev, [user.ID]: followerCount }));
        })
        .catch(error => {
          console.error('Error fetching followers count:', error);
        });
    });
  }, [users]);
  // ...

const handleFollow = (followeeId) => {
    axios.post('http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/follow', {
      follower_id: userId,
      followee_id: followeeId
    })
    .then(() => {
      setFollowees(currentFollowees => [...currentFollowees, followeeId]);
      setFollowersCount(prev => ({ ...prev, [followeeId]: (prev[followeeId] || 0) + 1 }));
    })
    .catch(error => {
      console.error('Error following user:', error);
    });
  };
  
  const handleUnfollow = (followeeId) => {
    axios.post('http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/unfollow', {
      follower_id: userId,
      followee_id: followeeId
    })
    .then(() => {
      setFollowees(currentFollowees => currentFollowees.filter(id => id !== followeeId));
      setFollowersCount(prev => ({ ...prev, [followeeId]: prev[followeeId] - 1 }));
    })
    .catch(error => {
      console.error('Error unfollowing user:', error);
    });
  };
  if (!users) {
    return <div className="users-container">Loading users...</div>;
  }

  if (users.length === 0) {
    return <div className="users-container">No users found.</div>;
  }
  return (
    <div className="users-container">
      {users.map(user => (
        user.ID !== userId && (
          <div key={user.ID} className="user-item">
            <div className="user-info">
              <span><strong>{user.Username}</strong></span>
            </div>
            <div className="user-followers">
              <span>{followersCount[user.ID] || 0} followers</span>
            </div>
            <div className="user-actions">
              {followees.includes(user.ID) ? (
                <button onClick={() => handleUnfollow(user.ID)}>Unfollow</button>
              ) : (
                <button onClick={() => handleFollow(user.ID)}>Follow</button>
              )}
              <button onClick={() => viewPosts(user.ID)}>View Posts</button>
            </div>
          </div>
        )
      ))}
    </div>
  );
};

export default Users;