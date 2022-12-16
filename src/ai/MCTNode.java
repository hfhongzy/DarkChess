package ai;

import chessComponent.ChessComponent;
import gameComponent.Chessboard;
import model.ChessStep;

import java.util.ArrayList;

import static java.lang.Math.*;
import static java.util.Collections.*;

public class MCTNode {
  public double p, q; //可以走到这个点的概率, 父亲到它的概率
  public double v, n;
  public MCTNode father;
  ChessStep chessStep;
  public ArrayList<MCTNode> child = new ArrayList<>();
  
  public int pointer = 0;
  final static double p_eps = 0.01;
  static int[] score = {0, 30, 10, 5, 5, 5, 1, 5};
  public byte [][]a = new byte[8][4];
  static final double c = sqrt(2.0);
  static double N0 = 0;
  public double ucb() {
    return 1 - v / n + c * sqrt(log(N0) / n);
  }
  public MCTNode(Chessboard chessboard, MCTNode father, ChessStep chessStep, double p, double q) {
    this.p = p;
    this.q = q;
    v = n = 0;
    this.father = father;
    this.chessStep = chessStep;
    for(int i = 0; i < 8; i ++) {
      for(int j = 0; j < 4; j ++) {
        ChessComponent chess = chessboard.getChessComponent(i, j);
        int type = chess.getID();
        int team = chessboard.playerStatus.getCurrentColor() == chess.getTeamColor() ? 1 : -1;
        int flip = chess.isReversal() ? 1 : 0;
        if(flip == 1) {
          a[i][j] = 0;
        } else {
          a[i][j] = (byte) (type + 1); //[1, 7]
          if(chess.isEaten())
            a[i][j] += 7;
          a[i][j] *= (byte) team;
        }
      }
    }
  }
  
  @Override
  public String toString() {
    String ans = "";
    for(int i = 0; i < 8; i ++)
      for(int j = 0; j < 4; j ++) {
        ans += a[i][j];
        ans += (j == 3 ? '\n' : ' ');
      }
    return ans;
  }
  
  public MCTNode(byte [][]a, MCTNode father, ChessStep chessStep, double p, double q) {
    this.p = p;
    this.q = q;
    v = n = 0;
    this.father = father;
    this.chessStep = chessStep;
    for(int i = 0; i < 8; i ++)
      for(int j = 0; j < 4; j ++)
        this.a[i][j] = (byte) -a[i][j];
  }
  static boolean valid(int x, int y) {
    return 0 <= x && x < 8 && 0 <= y && y < 4;
  }
  static boolean greater(int x, int y) { //x > 0, y < 0
    y = -y;
    return x == 6 ? (y == 1 || y == 6) : x <= y;
  }
  
  public void generate() {
    int []myCnt = {0, 1, 2, 2, 2, 2, 5, 2};
    int []yourCnt = {0, 1, 2, 2, 2, 2, 5, 2};
    int total = (1 + 2 + 2 + 2 + 2 + 5 + 2) * 2;
    for(int i = 0; i < 8; i ++) {
      for (int j = 0; j < 4; j++) {
        if (a[i][j] != 0) {
          total--;
          if (a[i][j] > 0) myCnt[a[i][j] > 7 ? a[i][j] - 7 : a[i][j]]--;
          else yourCnt[a[i][j] < -7 ? -a[i][j] - 7 : -a[i][j]]--;
        }
      }
    }
    for(int i = 0; i < 8; i ++) {
      for(int j = 0; j < 4; j ++) {
        if(1 <= a[i][j] && a[i][j] <= 6) {
          int []dx = {1, 0, -1, 0};
          int []dy = {0, 1, 0, -1};
          for(int d = 0; d < 4; d ++) {
            int x = i + dx[d], y = j + dy[d];
            if(valid(x, y)) {
              if(-7 <= a[x][y] && a[x][y] <= -1 && greater(a[i][j], a[x][y])) {
                byte tmp = a[x][y];
                a[x][y] = a[i][j];
                a[i][j] = tmp;
                a[i][j] -= 7;
                child.add(new MCTNode(a, this, new ChessStep(3, i, j, x, y), this.p, 1));
                a[i][j] += 7;
                tmp = a[i][j];
                a[i][j] = a[x][y];
                a[x][y] = tmp;
              } else if(a[x][y] < -7 || a[x][y] > 7){
                byte tmp = a[x][y];
                a[x][y] = a[i][j];
                a[i][j] = tmp;
                child.add(new MCTNode(a, this, new ChessStep(2, i, j, x, y), this.p, 1));
                tmp = a[x][y];
                a[x][y] = a[i][j];
                a[i][j] = tmp;
              }
            }
          }
        } else if(a[i][j] == 7) {
          int []dx = {1, 0, -1, 0};
          int []dy = {0, 1, 0, -1};
          for(int d = 0; d < 4; d ++) {
            int cnt = 0;
            int x = i, y = j;
            while(true) {
              x += dx[d]; y += dy[d];
              if(!valid(x, y)) break;
              if (-7 <= a[x][y] && a[x][y] <= 7) {
                cnt ++;
                if(cnt == 2) {
                  if(a[x][y] != 0) {//可能自己吃自己
                    byte tmp = a[x][y];
                    a[x][y] = a[i][j];
                    a[i][j] = (byte) (tmp < 0 ? tmp - 7 : tmp + 7);
                    child.add(new MCTNode(a, this, new ChessStep(3, i, j, x, y), this.p, 1));
                    a[i][j] = a[x][y];
                    a[x][y] = tmp;
                  }
                  break;
                }
              }
            }
          }
        } else if(a[i][j] == 0) {
          for(int u = 1; u < myCnt.length; u ++) if(myCnt[u] > 0) {
            MCTNode node = new MCTNode(a, this, new ChessStep(1, i, j), p * (myCnt[u] * 1.0 / total), myCnt[u] * 1.0 / total);
            if(node.p > p_eps)
              child.add(node);
          }
          for(int u = 1; u < yourCnt.length; u ++) if(yourCnt[u] > 0) {
            MCTNode node = new MCTNode(a, this, new ChessStep(1, i, j), p * (yourCnt[u] * 1.0 / total), yourCnt[u] * 1.0 / total);
            if(node.p > p_eps)
              child.add(node);
          }
        }
      }
    }
    shuffle(child);
  }
  double f(double x) {
    return 1 / (1 + exp(-x));
  }
  double evaluate() {
    double my = 0, your = 0;
    for (int i = 0; i < 8; i ++) {
      for(int j = 0; j < 4; j ++) {
        if (a[i][j] > 7) {
          your += score[a[i][j] - 7];
        } else if(a[i][j] < -7) {
          my += score[-a[i][j] - 7];
        }
      }
    }
    if(my >= 60) return 1;
    if(your >= 60) return 0;
    return f(2.2 * (my - your) / (60 - max(my, your)));
    /*
    double u = Math.min(my, your);
    double v = Math.max(my, your);
    return 0.5 + (my > your ? 0.5 : -0.5) * ( (v - u) / (60 - v) / 59 );
    */
  }
  static public void back(MCTNode u) {
    double v = u.v = u.evaluate();
    double n = u.n = 1;
    double base = 1;
//    N0 += u.p * 1;
    while(u.father != null) {
      v = 1 - v;
      base *= u.q;
      u = u.father;
      u.n += n * base;
      u.v += v * base;
    }
    N0 += base;
  }
  public MCTNode best2() {
    MCTNode res = null;
    for(int i = 0; i < pointer; i ++) {
      if(res == null || res.v / res.n > child.get(i).v / child.get(i).n) {
        res = child.get(i);
      }
    }
    return res;
  }
  public MCTNode best() {
    MCTNode res = null;
    for(int i = 0; i < pointer; i ++) {
      if(res == null || res.ucb() < child.get(i).ucb()) {
        res = child.get(i);
      }
    }
    return res;
  }
  static int co = 0;
  static public void dfs(MCTNode u) {
    if(u == null) return ;
//    System.out.printf("%d!", co++);
    if(u.pointer >= u.child.size()) {
      dfs(u.best());
      return ;
    }
    MCTNode next = u.child.get(u.pointer ++);
    back(next);
    next.generate();
  }
  
}
