# ScrollerImproveDemo
Demo for ViewPager2 + RecyclerView and ViewPager2 + ViewPager2 + RecyclerView 

- 优化 ViewPager2 + RecyclerView，ViewPager2 触发灵敏度。
- 优化 ViewPager2 + RecyclerView 切换逻辑。
  - 当 RecyclerView 在 fling 时，手指左右滑动 ViewPager2 时，会先停掉 RecyclerView 的 fling，再次滑动才能切换 ViewPager2。
  - 优化为即使 RecyclerView 在 fling ，手指左右滑动 ViewPager2 即可切换。体感上会更为顺畅。
- 解决 ViewPager2 + ViewPager2 嵌套时手势冲突问题。
  - 主要基于官方代码进行了部分修改，以兼容上述的优化逻辑。
  - https://github.com/android/views-widgets-samples/blob/master/ViewPager2/app/src/main/java/androidx/viewpager2/integration/testapp/NestedScrollableHost.kt

### 基本类
[CombineWithViewPager2RecyclerView](./app/src/main/java/xyz/juncat/scrollerimprove/CombineWithViewPager2RecyclerView.kt)
- 触发灵敏度修改
- 快速切换逻辑

[NestedScrollableHost](./app/src/main/java/xyz/juncat/scrollerimprove/NestedScrollableHost.kt)
- ViewPager2 嵌套冲突

## 原理
### 灵敏度
### 快速切换
### ViewPager2 嵌套处理