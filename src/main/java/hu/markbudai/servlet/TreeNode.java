/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.markbudai.servlet;

/**
 *
 * @author mark
 */
public class TreeNode {
    private char letter;
    private TreeNode leftChild = null;
    private TreeNode rightChild = null;
    
    
    public TreeNode(char letter){
        this.letter = letter;
        leftChild = null;
        rightChild = null;
    }
    
    public TreeNode getLeftChild(){
        return leftChild;
    }
    
    public TreeNode getRightChild(){
        return rightChild;
    }
    
    public void setLeftChild(TreeNode node){
        leftChild = node;
    }
    
    public void setRightChild(TreeNode node){
        rightChild = node;
    }
    
    public char getLetter(){
        return letter;
    }
}
