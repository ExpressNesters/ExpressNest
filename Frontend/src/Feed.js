import React, { useState, useEffect } from 'react';
import Post from './Post';
import './Feed.css';

const Feed = ({ userId }) => {
  const [postIds, setPostIds] = useState([]);
  const [visiblePosts, setVisiblePosts] = useState([]);
  const [visiblePostCount, setVisiblePostCount] = useState(2);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchFeed = async () => {
      setLoading(true);
      try {
        const response = await fetch(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/feeds/user/1`);
        const data = await response.json();
        if (data.postIds) {
          setPostIds(data.postIds);
        }
      } catch (error) {
        console.error('Error fetching feed:', error);
      }
      setLoading(false);
    };

    fetchFeed();
  }, [userId]);

  useEffect(() => {
    
    const fetchVisiblePosts = async () => {
      if (postIds.length === 0 || visiblePostCount > postIds.length) {
        return;
      }

      const idsToFetch = postIds.slice(0, visiblePostCount);
      const idsQuery = idsToFetch.map(id => `postIds=${id}`).join('&');
      const url = `http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/posts/by-postIds?${idsQuery}&size=${idsToFetch.length}&sort=createdAt`;

      try {
        const response = await fetch(url);
        const data = await response.json();
        if (data.postDTOs) {
          setVisiblePosts(data.postDTOs);
        }
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    };

    fetchVisiblePosts();
  }, [postIds, visiblePostCount]);

  const handleLoadMore = () => {
    if (visiblePostCount + 2 <= postIds.length) {
      setVisiblePostCount(prevCount => prevCount + 2);
    } else {
      // If there are fewer than 2 posts left, just show all remaining posts
      setVisiblePostCount(postIds.length);
    }
  };

  return (
    <div className="feed">
      {loading ? (
        <p>Loading...</p>
      ) : (
        <>
          {visiblePosts.map((post, index) => (
            <Post key={index} postData={post} />
          ))}
          {visiblePostCount < postIds.length && (
            <button onClick={handleLoadMore} className="load-more-btn">Load More</button>
          )}
        </>
      )}
    </div>
  );
};

export default Feed;
