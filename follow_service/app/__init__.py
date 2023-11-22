from flask import Flask
from neo4j import GraphDatabase
import os

app = Flask(__name__)

# Environment variable
uri = os.getenv("NEO4J_URI")

# Create a database driver instance without authentication
db_driver = GraphDatabase.driver(uri)

from app import routes
