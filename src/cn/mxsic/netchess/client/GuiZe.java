package cn.mxsic.netchess.client;

public class GuiZe {
	QiZi[][] qizi;//���ӵ�����
	boolean  canMove=false;//�Ƿ�����ƶ�
	int i;//���ӵ�x������
	int j;//���ӵ�y������
	public GuiZe(QiZi[][] qizi){
		this.qizi=qizi;
	}
	public boolean canMove(int startI,int startJ,int endI,int endJ,String name){
		int maxI;int minI;int maxJ;int minJ;//������
		canMove=true;
		if(startI>=endI){maxI=startI;minI=endI;}//��ʼ����Ĵ�С ��ϵ
		else{
			maxI=endI;minI=startI;
		}
		if(startJ>=endJ){maxJ=startJ;minJ=endJ;}//��ʼ����Ĵ�С ��ϵ
		else{
			maxJ=endJ;minJ=startJ;
		}
		if(name.equals("��")){this.ju(maxI,minI,maxJ,minJ);}//�жϲ�ͬ�����Ӷ�Ӧ��ͬ���ƶ�����
		else if(name.equals("��")){this.ma(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);}
		else if(name.equals("��")){this.xiang1(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);}
		else if(name.equals("��")){this.xiang2(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);}
		else if(name.equals("ʿ")||name.equals("��")){this.shi(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);}
		else if(name.equals("˧")||name.equals("��")){this.jiang(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);}
		else if(name.equals("��")||name.equals("�h")){this.pao(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);}
		else if(name.equals("��")){this.bing(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);}
		else if(name.equals("��")){this.zu(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);}
		return canMove;
	}
	private void zu(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		// TODO Auto-generated method stub
		if(startJ>4){//��û�й���
			if(startI!=endI){//���������ǰ��
				canMove=false;
			}
			if(endJ-startJ!=-1){//���������һ��
				canMove=false;
			}
		}else{//����Ѿ�������
			if(startI==endI){//�������ǰ��
				if(endJ-startJ!=-1){//���������һ��
					canMove=false;
				}
			}else if(startJ==endJ){//��� ���ߺ���
				if(maxI-minI!=1){//���������һ��
					canMove=false;
				}
			}else if(startI!=endI&&startJ!=endJ){//����ߵĲ�������Ҳ���Ǻ���
				canMove=false;
			}
			
		}
	}
	private void bing(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		// TODO Auto-generated method stub
		if(startJ<5){//��û�й���
			if(startI!=endI){//���������ǰ��
				canMove=false;
			}
			if(endJ-startJ!=1){//���������һ��
				canMove=false;
			}
		}else{//����Ѿ�������
			if(startI==endI){//�������ǰ��
				if(endJ-startJ!=1){//���������һ��
					canMove=false;
				}
			}else if(startJ==endJ){//��� ���ߺ���
				if(maxI-minI!=1){//���������һ��
					canMove=false;
				}
			}else if(startI!=endI&&startJ!=endJ){//����ߵĲ�������Ҳ���Ǻ���
				canMove=false;
			}
			
		}
	}
	private void pao(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		// TODO Auto-generated method stub
		if(maxI==minI){//�����һ��������
			if(qizi[endI][endJ]!=null){
				int count=0;//ͳ�ƴ����ϵ�������
				for (int i =minJ+1; i < maxJ; i++) {//����������
					if(qizi[minI][i]!=null){
						count++;
					}
				}
				if(count!=1){
					canMove=false;
				}
			}else if(qizi[endI][endJ]==null){//����յ�û������
				for (int i = minJ+1; i < maxJ; i++) {
					if(qizi[minI][i]!=null){
						canMove=false;
						break;
					}
				}
			}
		}else if(maxJ==minJ){//�����һ��������
			if(qizi[endI][endJ]!=null){
				int count=0;//ͳ�ƴ����ϵ�������
				for (int i =minI+1; i < maxI; i++) {//����������
					if(qizi[i][minJ]!=null){
						count++;
					}
				}
				if(count!=1){
					canMove=false;
				}
			}else if(qizi[endI][endJ]==null){//����յ�û������
				for (int i = minI+1; i < maxI; i++) {
					if(qizi[i][minJ]!=null){//�����ֹ��������
						canMove=false;
						break;
					}
				}
			}
		}else if(maxJ!=minJ&&maxI!=minI){//�������ͬһ������
			canMove=false;
		}
		
	}
	private void jiang(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		// TODO Auto-generated method stub
		int a=maxI-minI;int b=maxJ-minJ;//�õ�����������
		if((a==1&&b==0)||(a==0&&b==1)){//���������
			if(startJ>4){//������·��Ľ�
				if(endJ<7){
					canMove=false;//�·���Խ��
				}
			}else{//������Ϸ��Ľ�
				if(endJ>2){
					canMove=false;
				}
			}
			if(endI>5||endI<3){//�������Խ��
			canMove=false;
			}
		}else{//�������Сб��
			canMove=false;
		}
	}
	private void shi(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		// TODO Auto-generated method stub
		int a=maxI-minI;int b=maxJ-minJ;//�õ�����������
		if(a==1&&b==1){//���������
			if(startJ>4){//������·���
				if(endJ<7){
					canMove=false;//�·���Խ��
				}
			}else{//������Ϸ���
				if(endJ>2){
					canMove=false;
				}
			}
			if(endI>5||endI<3){//�������Խ��
			canMove=false;
			}
		}else{//�������Сб��
			canMove=false;
		}
	}
	private void xiang2(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		// TODO Auto-generated method stub
		int a=maxI-minI;int b=maxJ-minJ;//�õ�����������
		if(a==2&&b==2){//���������
			if(endJ<5){//���������
				canMove=false;
			}
			if(qizi[(maxI+minI)/2][(maxJ+minJ)/2]!=null){//��������м�������
				canMove=false;
			}
		}else{//����������֣�������
			canMove=false;
		}
	}
	private void xiang1(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		// TODO Auto-generated method stub
		int a=maxI-minI;int b=maxJ-minJ;//�õ�����������
		if(a==2&&b==2){//���������
			if(endJ>4){//���������
				canMove=false;
			}
			if(qizi[(maxI+minI)/2][(maxJ+minJ)/2]!=null){//��������м�������
				canMove=false;
			}
		}else{//����������֣�������
			canMove=false;
		}
	}
	private void ma(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		// TODO Auto-generated method stub
		int a=maxI-minI;int b=maxJ-minJ;//�����λ�ü�Ĳ�
		if(a==1&&b==2){//��������ŵ�����
			if(startJ>endJ){//�����������
				if(qizi[startI][startJ-1]!=null){
					canMove=false;
				}
			}else{//���������
				if(qizi[startI][startJ+1]!=null){
					canMove=false;
				}
			}
		}else if(a==2&&b==1){//������ŵ�����
			if(startI>endI){//����Ǵ�������
			if(qizi[startI-1][startJ]!=null){
				canMove=false;
			}}
			else {//�����������
				if(qizi[startI+1][startJ]!=null){
					canMove=false;
				}
			}
		}else if(!((a==2&&b==1)||(a==1&&b==2))){//�����������
			canMove=false;
		}
	}
	private void ju(int maxI, int minI, int maxJ, int minJ) {
		// TODO Auto-generated method stub
		if(maxI==minI){
			for (int i = minJ+1; i < maxJ; i++) {
				if(qizi[maxI][i]!=null){
				canMove=false;
				break;
				}
			}
		}else if(maxJ==minJ){
			for (int i = minI+1; i < maxI; i++) {
				if(qizi[i][maxJ]!=null){
				canMove=false;
				break;
				}
			}
		}
		else if(maxI!=minI&&maxJ!=minJ){
			canMove=false;
		}
	}

}
