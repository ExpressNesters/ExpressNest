
import React, { useState, useEffect } from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEdit, faTrash } from '@fortawesome/free-solid-svg-icons';
import CommentForm from './components/CommentForm';
import CommentsList from './components/CommentsList';
import { useSelector } from 'react-redux';
import './Post.css'; // Make sure your CSS file is correctly referenced

const Post = ({ postData, onPostUpdated }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [editText, setEditText] = useState(postData.postText);
  const [refreshComments, setRefreshComments] = useState(false);
  const [username, setUsername] = useState('');

  // Retrieve currentUserId from Redux state
  const currentUserId = useSelector(state => state.user);
  const userRole = useSelector(state => state.role);
  const handleEditClick = () => {
    setIsEditing(true);
  };
  useEffect(() => {
    const fetchUsername = async () => {
      try {
        const response = await fetch(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/users/${postData.userId}`);
        if (!response.ok) {
          throw new Error('User data fetch failed');
        }
        const data = await response.json();
        setUsername(data.Username);
      } catch (error) {
        console.error('Error fetching user data:', error);
      }
    };

    fetchUsername();
  }, [postData.userId]);



  const handleSaveClick = async () => {
    const updatedPost = {
      userId: postData.userId,
      postId: postData.postId,
      postText: editText,
      privacy: "Public"
    };

    try {
      const response = await fetch(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/posts/${postData.postId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedPost),
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }

      setIsEditing(false);
      onPostUpdated(); // Notify parent component to refresh posts
    } catch (error) {
      console.error('Failed to update post:', error);
    }
  };

  // Inside your Post component
  const handleDeleteClick = async () => {
    try {
        console.log(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/posts/${postData.postId}`)
      const response = await fetch(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/posts/${postData.postId}`, {
        method: 'DELETE'
      });
      console.log(response)
  
      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }
  
      onPostUpdated(); // Notify parent component to refresh posts after deletion
    } catch (error) {
      console.error('Failed to delete post:', error);
    }
  };

  const refreshCommentsCallback = () => {
    setRefreshComments(prev => !prev); // Toggle to trigger re-fetch in CommentsList
  };

  if (postData.deletedAt !== null) {
    return null;
  }

  return (
    <div className="post">
      {isEditing ? (
        <>
          <textarea value={editText} onChange={(e) => setEditText(e.target.value)} />
          <button onClick={handleSaveClick}>Save</button>
          <button onClick={() => setIsEditing(false)}>Cancel</button>
        </>
      ) : (
        <>
          <div className="post-content">
            <p>{postData.postText}</p>
            <div className="posted-by">
        Posted by <span>{postData.userId === currentUserId ? 'You' : username}</span>
      </div>
          </div>
          <div className="post-actions">
            {(currentUserId === postData.userId || userRole === 'ADMIN')  && (
              <>
                <FontAwesomeIcon icon={faEdit} onClick={handleEditClick} />
                <FontAwesomeIcon icon={faTrash} onClick={handleDeleteClick} />
              </>
            )}
          </div>
          <CommentsList postId={postData.postId} userId={currentUserId} refreshTrigger={refreshComments} />
        </>
      )}
    </div>
  );
  
};

export default Post;