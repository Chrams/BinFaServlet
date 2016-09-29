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
public class BinaryTree {
    
    private TreeNode tree = null;
    private int depth, sumavg, avgdb;
    private double varsum;
    protected TreeNode root = new TreeNode('/');
    int maxDepth;
    double avg, var;
    
    public BinaryTree(){
        tree = root;
    }
    
    public void processBit(char c){
        if(c=='0'){
            if(tree.getLeftChild() == null){
                TreeNode node = new TreeNode('0');
                tree.setLeftChild(node);
                tree = root;
            } else {
                tree = tree.getLeftChild();
            }
        } else {
            if(tree.getRightChild() == null){
                TreeNode node = new TreeNode('1');
                tree.setRightChild(node);
                tree = root;
            } else {
                tree = tree.getRightChild();
            }
        }
    }
    
    public void print(){
        depth = 0;
        print(root,new java.io.PrintWriter(System.out));
    }
    
    public void print(java.io.PrintWriter os){
        depth = 0;
        print(root,os);
    }
    
    public void print(TreeNode node, java.io.PrintWriter os){
        if(node != null){
            ++depth;
            print(node.getRightChild(), os);
            for(int i=0;i<depth;++i){
                os.print("---");
            }
            os.print(node.getLetter());
            os.print("(");
            os.print(depth-1);
            os.println(")<br/>");
            print(node.getLeftChild(),os);
            --depth;
        }
    }
        
        public int getDepth(){
            depth = maxDepth = 0;
            rDepth(root);
            return maxDepth -1;
        }
        
        public double getAvg(){
            depth = sumavg = avgdb = 0;
            rAvg(root);
            avg = ((double)sumavg)/avgdb;
            return avg;
        }
        
        public double getVar(){
            avg = getAvg();
            varsum = 0.0;
            depth = avgdb = 0;
            rVar(root);
            if(avgdb-1>0){
                var = Math.sqrt(varsum / (avgdb-1));
            } else {
                var = Math.sqrt(varsum);
            }
            return var;
        }
        
        public void rDepth(TreeNode node){
            if(node!=null){
                ++depth;
                if(depth > maxDepth){
                    maxDepth = depth;
                }
                rDepth(node.getLeftChild());
                rDepth(node.getRightChild());
                --depth;
            }
        }
        
        public void rAvg(TreeNode node){
            if(node!=null){
                ++depth;
                rAvg(node.getLeftChild());
                rAvg(node.getRightChild());
                --depth;
                if(node.getLeftChild() == null && node.getRightChild() == null){
                    ++avgdb;
                    sumavg += depth;
                }
            }
        }
        
        public void rVar(TreeNode node){
            if(node != null){
                ++depth;
                rVar(node.getLeftChild());
                rVar(node.getRightChild());
                --depth;
                if(node.getLeftChild() == null && node.getRightChild() == null){
                    ++avgdb;
                    varsum += ((depth-avg)*(depth-avg));
                }
            }
        }
        
        @Override
        public String toString(){
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            print(pw);
            pw.println("depth = " + getDepth() + "<br/>");
            pw.println("mean = " + getAvg() + "<br/>");
            pw.println("var = " + getVar() + "<br/>");
            pw.flush();
            pw.close();
            return sw.toString();
        }
        
        
}
