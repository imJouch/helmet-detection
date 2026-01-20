# 非机动车头盔佩戴检测系统

## Helmet Detection System for Non-Motor Vehicles

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Python](https://img.shields.io/badge/python-3.8%2B-blue.svg)](https://www.python.org/)
[![YOLOv5](https://img.shields.io/badge/YOLOv5-latest-informational.svg)](https://github.com/ultralytics/yolov5)

> 一个基于深度学习的非机动车头盔佩戴检测系统，支持PC端实时检测和移动端部署

## 📋 项目简介

本项目基于YOLOv5目标检测框架，针对非机动车(自行车、电动车等)骑行者的头盔佩戴情况进行实时检测。支持图像、视频流处理，以及Android移动端应用部署。

### 核心特性

- **高精度检测**：基于YOLOv5深度学习模型，检测准确率高
- **多平台支持**：提供PC端Python版本和移动端Android应用
- **实时处理**：支持摄像头实时视频流检测
- **易于部署**：支持Docker容器化部署
- **完整工具链**：包含数据标注、模型训练、导出等全流程工具

## 📁 项目结构

```
helmet-detection/
├── 头盔检测电脑端yolov5/          # PC端YOLOv5检测系统
│   ├── detect.py                 # 推理脚本（图像/视频检测）
│   ├── train.py                  # 模型训练脚本
│   ├── test.py                   # 模型测试脚本
│   ├── export.py                 # 模型导出脚本（ONNX/TorchScript等）
│   ├── models/                   # 模型定义（YOLOv5结构）
│   ├── utils/                    # 工具函数集
│   ├── weights/                  # 预训练模型权重
│   ├── requirements.txt           # Python依赖
│   ├── tutorial.ipynb            # 使用教程
│   └── Dockerfile                # Docker配置
├── 头盔检测app/                   # 移动端Android应用
│   ├── app/                      # Android App主模块
│   ├── build.gradle              # Gradle构建配置
│   └── README.md                 # 移动端说明文档
└── README.md                     # 项目说明文档
```

## 🚀 快速开始

### PC端使用

#### 前置要求

- Python 3.8+
- CUDA 11.0+ (GPU加速，可选)
- PyTorch

#### 安装依赖

```bash
cd 头盔检测电脑端yolov5
pip install -r requirements.txt
```

#### 推理示例

```bash
# 检测图像
python detect.py --source image.jpg --conf 0.25

# 检测视频
python detect.py --source video.mp4 --conf 0.25

# 实时摄像头检测
python detect.py --source 0 --conf 0.25
```

#### 参数说明

| 参数         | 说明                       | 默认值            |
| ------------ | -------------------------- | ----------------- |
| `--source`   | 输入源（图像/视频/摄像头） | -                 |
| `--conf`     | 置信度阈值                 | 0.25              |
| `--weights`  | 模型权重路径               | `weights/best.pt` |
| `--img-size` | 推理图像尺寸               | 640               |

### 模型训练

```bash
python train.py --img 640 --batch 16 --epochs 100 --data data.yaml --weights yolov5s.pt
```

### 模型导出

```bash
python export.py --weights weights/best.pt --include onnx,torchscript
```

### 移动端应用

详见 [头盔检测app/README.md](头盔检测app/README.md)

## 💡 技术方案

### 模型架构

- **基础模型**：YOLOv5 (可选s/m/l/x版本)
- **输入尺寸**：640×640
- **检测类别**：
  - 佩戴头盔
  - 未佩戴头盔

### 数据处理

- **数据增强**：Mosaic混合、随机翻转、颜色抖动等
- **归一化**：ImageNet统计数据
- **标注格式**：COCO/Pascal VOC

### 推理优化

- **量化**：支持INT8量化
- **加速框架**：ONNX Runtime、TensorRT (PC端)
- **移动适配**：NCNN框架 (Android端)

## 📊 性能指标

| 模型    | 精度(mAP@0.5) | 速度(ms) | 参数量 |
| ------- | ------------- | -------- | ------ |
| YOLOv5s | 85%           | 15       | 7.2M   |
| YOLOv5m | 88%           | 25       | 20.9M  |
| YOLOv5l | 90%           | 40       | 46.6M  |

_注：基于GPU推理，数据可能因环境而异_

## 🔧 使用示例

### Python代码集成

```python
import torch
from models.yolo import Detect

# 加载模型
model = torch.hub.load('ultralytics/yolov5', 'custom', path='weights/best.pt')

# 执行推理
results = model('image.jpg')

# 获取检测结果
print(results.pandas().xyxy[0])
```

## 📱 移动端特性

- **实时检测**：支持摄像头实时预览
- **离线运行**：模型本地部署，无需网络连接
- **低延迟**：优化的NCNN推理框架
- **多架构支持**：ARMv7/ARMv8

## 🐳 Docker部署

```bash
cd 头盔检测电脑端yolov5
docker build -t helmet-detection:latest .
docker run --gpus all -v $(pwd):/workspace helmet-detection:latest \
  python detect.py --source video.mp4
```

## 📝 数据集格式

项目支持标准COCO和Pascal VOC格式数据集：

```
dataset/
├── images/
│   ├── train/
│   └── val/
└── labels/
    ├── train/
    └── val/
```

## 🤝 贡献指南

欢迎提交Issue和Pull Request来改进本项目！

## 📄 许可证

本项目采用 [MIT License](LICENSE)

## 📚 相关资源

- [YOLOv5官方仓库](https://github.com/ultralytics/yolov5)
- [NCNN推理框架](https://github.com/tencent/ncnn)
- [PyTorch官方文档](https://pytorch.org/)

## 🙋 常见问题

**Q: 如何使用自己的数据集训练模型？**
A: 准备好符合COCO/VOC格式的数据集，修改数据配置文件`data.yaml`，运行训练命令即可。

**Q: 模型精度不够高怎么办？**
A: 可以尝试更大的模型(m/l/x)、增加训练轮数、或使用更多高质量的训练数据。

**Q: 移动端推理速度如何优化？**
A: 可以使用量化模型、或选择更小的YOLOv5s模型版本。

## 📞 联系方式

如有问题或建议，欢迎通过以下方式联系：

- 提交GitHub Issue
- 发送邮件联系

---

**最后更新**: 2026年1月
**维护者**: [Jouch]
