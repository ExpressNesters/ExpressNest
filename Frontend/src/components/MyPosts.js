// src/components/MyPosts.js
import React, { useState, useEffect } from 'react';
import CreatePostForm from './CreatePostForm';
import Post from '../Post';
import './MyPosts.css'; // Ensure this is correctly imported

const MyPosts = () => {
  const [posts, setPosts] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const userId = 1; // Replace with actual user ID

  useEffect(() => {
    fetchPosts(currentPage);
  }, [currentPage]);

  const fetchPosts = async (page) => {
    try {
      const response = await fetch(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/posts/user/${userId}?page=${page}&size=10&sort=createdAt,desc`);
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const data = await response.json();
      setPosts(data.postDTOs);
      setTotalPages(data.totalPages);
    } catch (error) {
      console.error('Failed to fetch posts:', error);
    }
  };

  const handlePostCreated = () => {
    fetchPosts(0);
  };
  const handlePostUpdated = () => {
    fetchPosts(currentPage); // Re-fetch the posts on update
  };

// Inside MyPosts.js

// Inside MyPosts.js

return (
    <div>
      <CreatePostForm onPostCreated={handlePostCreated} />
  
      {/* Displaying each post */}
      <div>
        {posts.map(post => (
          <Post 
            key={post.postId} 
            postData={post} 
            onPostUpdated={handlePostUpdated} 
          />
        ))}
      </div>
  
      {/* Pagination */}
      <div className="pagination">
        {Array.from({ length: totalPages }, (_, index) => (
          <button 
            key={index} 
            onClick={() => setCurrentPage(index)} 
            className={currentPage === index ? 'active' : ''}
          >
            {index + 1}
          </button>
        ))}
      </div>
    </div>
  );
  
  
};

export default MyPosts;
