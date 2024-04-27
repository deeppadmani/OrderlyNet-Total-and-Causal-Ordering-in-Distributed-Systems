#!/bin/bash

# Initialize a new directory for your project
echo "Creating a new project directory..."
cd AOS_Project3

# Initialize a Git repository
#echo "Initializing a new Git repository..."
#git init

# Create some initial files for your project (optional)
#echo "Creating initial files..."
3touch README.md
#echo "Hello, this is my AOS_Project3!" > README.md

# Add the files to the staging area
echo "Adding files to the staging area..."
git add .

# Commit the changes
echo "Committing initial changes..."
git commit -m "Initial commit"

# Optionally, set up a remote repository (e.g., on GitHub)
# Replace 'username' and 'repository' with your GitHub username and repository name
#echo "Setting up a remote repository..."
#git remote add origin https://github.com/deeppadmani/OrderlyNet-Total-and-Causal-Ordering-in-Distributed-Systems.git

# Push the changes to the remote repository
echo "Pushing changes to the remote repository..."
git push -u origin master

echo "Project setup complete!"
