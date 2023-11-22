from app import db_driver

def follow_user(follower_id, followee_id):
    with db_driver.session() as session:
        session.run("MERGE (a:User {id: $follower_id}) "
                    "MERGE (b:User {id: $followee_id}) "
                    "MERGE (a)-[:FOLLOWS]->(b)",
                    follower_id=follower_id, followee_id=followee_id)

def unfollow_user(follower_id, followee_id):
    with db_driver.session() as session:
        session.run("MATCH (a:User {id: $follower_id})-[r:FOLLOWS]->(b:User {id: $followee_id}) "
                    "DELETE r",
                    follower_id=follower_id, followee_id=followee_id)

def get_followers(user_id):
    with db_driver.session() as session:
        result = session.run("MATCH (a:User)-[:FOLLOWS]->(b:User {id: $user_id}) "
                             "RETURN a.id",
                             user_id=user_id)
        followers_list = [record["a.id"] for record in result]
        return {"userId": user_id, "followerIds": followers_list}
