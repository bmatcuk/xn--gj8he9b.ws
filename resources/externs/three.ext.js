/**
 * @fileoverview Externs for three.js specific to 🍩😻.ws
 * @see https://threejs.org/
 * @see https://github.com/spite/THREE.DecalGeometry
 * @externs
 */

/**
 * @constructor
 * @extends {THREE.Light}
 * @param {number=} color
 * @param {number=} intensity
 */
THREE.AmbientLight = function(color, intensity) {};



/** @constructor */
THREE.Box3 = function() {};

/** */
THREE.Box3.prototype.getCenter = function() {};

/** @param {THREE.Object3D} object */
THREE.Box3.prototype.setFromObject = function(object) {};



/** @constructor */
THREE.BufferGeometry = function() {};

/** @param {THREE.Geometry} geometry */
THREE.BufferGeometry.prototype.fromGeometry = function(geometry) {};



/**
 * @constructor
 * @extends {THREE.Object3D}
 */
THREE.Camera = function() {};



/**
 * @constructor
 * @param {(string|number)=} r
 * @param {number=} g
 * @param {number=} b
 */
THREE.Color = function(r, g, b) {};



/**
 * @constructor
 * @extends {THREE.Geometry}
 * @param {THREE.Mesh} mesh
 * @param {THREE.Vector3} position
 * @param {THREE.Vector3} direction
 * @param {THREE.Vector3} dimensions
 * @param {THREE.Vector3} check
 */
THREE.DecalGeometry = function(mesh, position, direction, dimensions, check) {};



/** @constructor */
THREE.Euler = function() {};

/** @type {number} */
THREE.Euler.prototype.x;

/** @type {number} */
THREE.Euler.prototype.y;



/**
 * @constructor
 */
THREE.Geometry = function() {};



/**
 * @constructor
 * @extends {THREE.Object3D}
 */
THREE.Group = function() {};



/** @constructor */
THREE.JSONLoader = function() {};

/**
 * @param {string} url
 * @param {function((THREE.Object3D|Array<THREE.Object3D>))} onLoad
 */
THREE.JSONLoader.prototype.load = function(url, onLoad) {};



/**
 * @record
 * @constructor
 * @param {number=} color
 * @param {number=} intensity
 */
THREE.Light = function(color, intensity) {};

/** @type {THREE.Color} */
THREE.Light.prototype.color;

/** @type {number} */
THREE.Light.prototype.intensity;



/**
 * @record
 * @constructor
 */
THREE.Material = function() {};

/** @type {Vector2} */
THREE.Material.prototype.normalScale;

/** @param {THREE.Material} material */
THREE.Material.prototype.copy = function(material) {};

/** @param {{}} values */
THREE.Material.prototype.setValues = function(values) {};



/**
 * @constructor
 * @extends {THREE.Object3D}
 */
THREE.Mesh = function() {};

/** @type {(THREE.Geometry|THREE.BufferGeometry)} */
THREE.Mesh.prototype.geometry;

/** @type {THREE.Material} */
THREE.Mesh.prototype.material;



/**
 * @constructor
 * @extends {THREE.Material}
 */
THREE.MeshPhongMaterial = function() {};



/**
 * @record
 * @constructor
 */
THREE.Object3D = function() {};

/** @type {string} */
THREE.Object3D.prototype.name;

/** @type {THREE.Vector3} */
THREE.Object3D.prototype.position;

/** @type {THREE.Euler} */
THREE.Object3D.prototype.rotation;

/** @param {...THREE.Object3D} objects */
THREE.Object3D.prototype.add = function(objects) {};

/** @param {string} name */
THREE.Object3D.prototype.getObjectByName = function(name) {};

/** @param {...THREE.Object3D} objects */
THREE.Object3D.prototype.remove = function(objects) {};



/**
 * @constructor
 * @extends {THREE.Camera}
 */
THREE.PerspectiveCamera = function() {};

/** @type {number} */
THREE.PerspectiveCamera.prototype.aspect = 1;

/** @type {number} */
THREE.PerspectiveCamera.prototype.far = 2000;

/** @type {number} */
THREE.PerspectiveCamera.prototype.fov = 50;

/** @type {number} */
THREE.PerspectiveCamera.prototype.near = 0.1;

/** */
THREE.PerspectiveCamera.prototype.updateProjectionMatrix = function() {};



/**
 * @constructor
 * @extends {THREE.Light}
 */
THREE.PointLight = function() {};

/** @type {number} */
THREE.PointLight.prototype.distance = 0.0;



/**
 * @constructor
 * @extends {THREE.Object3D}
 */
THREE.Scene = function() {};



/** @constructor */
THREE.Texture = function() {};



/** @constructor */
THREE.TextureLoader = function() {};

/**
 * @param {string} url
 * @param {function(THREE.Texture)} onLoad
 */
THREE.TextureLoader.prototype.load = function(url, onLoad) {};



/**
 * @constructor
 * @param {number=} x
 * @param {number=} y
 */
THREE.Vector2 = function(x, y) {};



/**
 * @constructor
 * @param {number=} x
 * @param {number=} y
 * @param {number=} z
 */
THREE.Vector3 = function(x, y, z) {};



/** @constructor */
THREE.WebGLRenderer = function() {};

/** @type {Node} */
THREE.WebGLRenderer.prototype.domElement;

/**
 * @param {THREE.Scene} scene
 * @param {THREE.Camera} camera
 */
THREE.WebGLRenderer.prototype.render = function(scene, camera) {};

/**
 * @param {number} width
 * @param {number} height
 */
THREE.WebGLRenderer.prototype.setSize = function(width, height) {};

