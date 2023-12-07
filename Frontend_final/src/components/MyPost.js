import React, { useState, useEffect } from 'react';
import CreatePostForm from './CreatePostForm';
import Post from '../Post';
import './MyPosts.css';

const MyPost = ({userId}) => {
  const [allPosts, setAllPosts] = useState([]);
  const [currentPosts, setCurrentPosts] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const pageSize = 10;
  console.log("Inside MyPost")
  console.log(userId)
 // const userId = 1; // Replace with actual user ID

  useEffect(() => {
    fetchAllPages(0, []);
  }, []);

  const fetchAllPages = async (page, accumulatedPosts) => {
    try {
      const response = await fetch(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/posts/user/${userId}?page=${page}&size=${pageSize}&sort=createdAt,desc`);
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const data = await response.json();
      const newPosts = accumulatedPosts.concat(data.postDTOs.filter(post => post.deletedAt === null));
      
      if (page < data.totalPages - 1) {
        fetchAllPages(page + 1, newPosts);
      } else {
        setAllPosts(newPosts);
        setTotalPages(Math.ceil(newPosts.length / pageSize));
        setCurrentPage(0);
        updateCurrentPosts(0, newPosts);
      }
    } catch (error) {
      console.error('Failed to fetch posts:', error);
    }
  };

  const updateCurrentPosts = (page, posts) => {
    const indexOfLastPost = (page + 1) * pageSize;
    const indexOfFirstPost = indexOfLastPost - pageSize;
    setCurrentPosts(posts.slice(indexOfFirstPost, indexOfLastPost));
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
    updateCurrentPosts(page, allPosts);
  };

  const handlePostCreatedOrDeleted = () => {
    fetchAllPages(0, []);
  };

  return (
    <div>
      <CreatePostForm onPostCreated={handlePostCreatedOrDeleted} />
  
      <div className="my-posts">
        {currentPosts.map(post => (
          <Post key={post.postId} postData={post} onPostUpdated={handlePostCreatedOrDeleted} />
        ))}
      </div>
  
      <div className="pagination">
        {Array.from({ length: totalPages }, (_, index) => (
          <button 
            key={index} 
            onClick={() => handlePageChange(index)} 
            className={currentPage === index ? 'active' : ''}
          >
            {index + 1}
          </button>
        ))}
      </div>
    </div>
  );
};

export default MyPost;